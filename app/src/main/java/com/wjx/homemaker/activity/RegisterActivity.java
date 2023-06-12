package com.wjx.homemaker.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wjx.homemaker.R;
import com.wjx.homemaker.dialog.CustomProgressDialog;
import com.wjx.homemaker.entity.BaseEntity;
import com.wjx.homemaker.utils.Base64Util;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.GsonUtil;

import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
import cn.bmob.newsmssdk.listener.SMSCodeListener;
import cn.bmob.newsmssdk.listener.VerifySMSCodeListener;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edit_username;
    private EditText edit_password;
    private EditText edit_password2;
    private EditText edit_phone;
    private EditText edit_test;

    private TextView tv_test;

    private Button btn_register;

    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }


    private void initView() {
//        BmobSMS.initialize(MyApplication.getInstance(), "b8508b06477abbccb1afc8e11f4ca099" , new MySMSCodeListener());

        edit_username = (EditText) findViewById(R.id.register_edit_username);
        edit_password = (EditText) findViewById(R.id.register_edit_password);
        edit_password2 = (EditText) findViewById(R.id.register_edit_password2);
        edit_phone = (EditText) findViewById(R.id.register_edit_phone);
        edit_test = (EditText) findViewById(R.id.register_edit_test);

        tv_test = (TextView) findViewById(R.id.register_tv_test);

        btn_register = (Button) findViewById(R.id.login_btn_register);


        time = new TimeCount(60000, 1000);



        tv_test.setOnClickListener(this);
        btn_register.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_tv_test:

                time.start();
                doPhone();
                break;
            case R.id.login_btn_register:

                doRegister();
                break;
        }
    }

    private void doPhone() {

        String phone = edit_phone.getText().toString();

        BmobSMS.requestSMSCode(this, phone, "homemake",new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId, cn.bmob.newsmssdk.exception.BmobException ex) {
                // TODO Auto-generated method stub
                if(ex==null){//验证码发送成功
                    Log.i("bmob", "短信id："+smsId);//用于查询本次短信发送详情
                }
            }

        });

    }


    private void doRegister() {

        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
                this);
        customProgressDialog.setMessage("正在注册...");
        customProgressDialog.show();

        final String username = edit_username.getText().toString();
        final String password = edit_password.getText().toString();
        String password2 = edit_password2.getText().toString();
        final String phone = edit_phone.getText().toString();
        String test = edit_test.getText().toString();

        if (username.equals("") || password.equals("") || phone.equals("") || test.equals("")) {
            Toast.makeText(getApplicationContext(), "输入不能为空!", Toast.LENGTH_SHORT).show();
            if (customProgressDialog != null
                    && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();
            }
            return;
        }

        if (!password.equals(password2)) {
            Toast.makeText(getApplicationContext(), "密码输入不同!", Toast.LENGTH_SHORT).show();
            if (customProgressDialog != null
                    && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();
            }
            return;
        }



        BmobSMS.verifySmsCode(this,phone, test, new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {
                // TODO Auto-generated method stub
                if(ex==null){//短信验证码已验证成功
                    Log.i("bmob", "验证通过");
                }else{
                    Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                    Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
                    if (customProgressDialog != null
                            && customProgressDialog.isShowing()) {
                        customProgressDialog.dismiss();
                    }
                    return;
                }
            }
        });
        Bitmap decodeResource = BitmapFactory.decodeResource(this.getResources(), R.mipmap.register_icon);
        final String image = Base64Util.bitmapToBase64(decodeResource);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    EMClient.getInstance().createAccount(username, password);

                    HttpUtils httpUtils = new HttpUtils();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("username",username);
                    params.addBodyParameter("password",password);
                    params.addBodyParameter("phone",phone);
                    params.addBodyParameter("image", image);
                    httpUtils.send(HttpMethod.POST, Config.REGISTER, params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            String json = responseInfo.result;
                            BaseEntity baseEntity = GsonUtil.fromJson(json, BaseEntity.class);

                            if (customProgressDialog != null
                                    && customProgressDialog.isShowing()) {
                                customProgressDialog.dismiss();
                            }

                            if(baseEntity.errcode == 0){
                                Toast.makeText(getApplicationContext(),
                                        "注册成功",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "注册失败:"+baseEntity.errmsg,
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(HttpException exception, String msg) {
                            Toast.makeText(getApplicationContext(),
                                    exception.getMessage() + " = " + msg,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                    if (customProgressDialog != null
                            && customProgressDialog.isShowing()) {
                        customProgressDialog.dismiss();
                    }
                    return;
                }
            }
        }).start();

    }



    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_test.setTextColor(Color.parseColor("#cedbf6"));
            tv_test.setClickable(false);
            tv_test.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {

            tv_test.setText("重新获取验证码");
            tv_test.setClickable(true);
            tv_test.setTextColor(Color.parseColor("#fbe0a6"));
        }
    }

    class MySMSCodeListener implements SMSCodeListener {

        @Override
        public void onReceive(String content) {
            if(edit_phone != null){
                edit_phone.setText(content);
            }
        }

    }

}
