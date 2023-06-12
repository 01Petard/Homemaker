package com.wjx.homemaker.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
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
import com.wjx.homemaker.entity.UserEntity;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.GsonUtil;
import com.wjx.homemaker.utils.SharedUtils;

import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView tv_register;

    private EditText edit_id;
    private EditText edit_password;

    private Button btn_go;

    private CheckBox ck_c;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.CALL_PHONE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        verifyStoragePermissions(this);
    }

    private void initView() {

        tv_register = (TextView) findViewById(R.id.login_tv_register);
        edit_id = (EditText) findViewById(R.id.login_edit_id);
        edit_password = (EditText) findViewById(R.id.login_edit_password);
        btn_go = (Button) findViewById(R.id.login_btn_go);
        ck_c = (CheckBox) findViewById(R.id.login_ck);

        tv_register.setOnClickListener(this);
        btn_go.setOnClickListener(this);

        if (SharedUtils.getClick(getBaseContext())) {
            ck_c.setChecked(true);
        } else {
            ck_c.setChecked(false);
        }

        if (ck_c.isChecked()) {
            edit_id.setText(SharedUtils.getName(getBaseContext()));
            edit_password.setText(SharedUtils.getPass(getBaseContext()));
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_tv_register:

                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.login_btn_go:

                doLogin();
                break;
        }
    }
    public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                PERMISSIONS_STORAGE[0]);
        int permission1 = ActivityCompat.checkSelfPermission(activity,
                PERMISSIONS_STORAGE[1]);
        int permission2 = ActivityCompat.checkSelfPermission(activity,
                PERMISSIONS_STORAGE[2]);
        int permission3 = ActivityCompat.checkSelfPermission(activity,
                PERMISSIONS_STORAGE[3]);
        int permission4 = ActivityCompat.checkSelfPermission(activity,
                PERMISSIONS_STORAGE[4]);

        if (permission != PackageManager.PERMISSION_GRANTED||permission1 != PackageManager.PERMISSION_GRANTED||permission2 != PackageManager.PERMISSION_GRANTED||permission3 != PackageManager.PERMISSION_GRANTED||permission4 != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void doLogin() {

        final String username = edit_id.getText().toString();
        final String password = edit_password.getText().toString();

        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
                this);
        customProgressDialog.setMessage("正在登录...");
        customProgressDialog.show();

        if (username.equals("") || password.equals("")) {
            if (customProgressDialog != null
                    && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();

            }
            Toast.makeText(LoginActivity.this, "输入不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUtils httpUtils = new HttpUtils();
                RequestParams params = new RequestParams();
                params.addBodyParameter("username", username);
                params.addBodyParameter("password", password);
                httpUtils.send(HttpMethod.POST, Config.LOGIN, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String json = responseInfo.result;
                        final UserEntity userEntity = GsonUtil.fromJson(json, UserEntity.class);
                        if (userEntity.errcode == 0) {

                            Log.i("wjx12345", "onSuccess: "+json);
                            if (ck_c.isChecked()) {
                                SharedUtils.putName(getBaseContext(), username);
                                SharedUtils.putPass(getBaseContext(), password);
                                SharedUtils.putClick(getBaseContext(), true);
                            } else {
                                SharedUtils.putName(getBaseContext(), null);
                                SharedUtils.putPass(getBaseContext(), null);
                                SharedUtils.putClick(getBaseContext(), false);
                            }

                            EMClient.getInstance().login(username, password, new EMCallBack() {
                                @Override
                                public void onSuccess() {

                                    if (ck_c.isChecked()) {
                                        SharedUtils.putName(getBaseContext(), username);
                                        SharedUtils.putPass(getBaseContext(), password);
                                        SharedUtils.putClick(getBaseContext(), true);
                                    } else {
                                        SharedUtils.putName(getBaseContext(), null);
                                        SharedUtils.putPass(getBaseContext(), null);
                                        SharedUtils.putClick(getBaseContext(), false);
                                    }
                                    try {
                                        List<String> userNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                                        MyApplication.userNames = userNames;
                                    } catch (HyphenateException e) {
                                        e.printStackTrace();
                                    }
                                    if (customProgressDialog != null
                                            && customProgressDialog.isShowing()) {
                                        customProgressDialog.dismiss();
                                    }


                                    MyApplication.userEntity = userEntity;

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                    finish();
                                }

                                @Override
                                public void onError(int i, String s) {

                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        Toast.makeText(getApplicationContext(),
                                exception.getMessage() + " = " + msg,
                                Toast.LENGTH_LONG).show();
                        if (customProgressDialog != null
                                && customProgressDialog.isShowing()) {
                            customProgressDialog.dismiss();
                        }
                        return;
                    }
                });


            }
        }).start();


    }


}
