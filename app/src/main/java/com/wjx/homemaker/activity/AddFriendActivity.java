package com.wjx.homemaker.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.R;
import com.wjx.homemaker.dialog.CustomProgressDialog;
import com.wjx.homemaker.entity.BaseEntity;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.GsonUtil;

/**
 * Created by admin on 2017/7/17.
 */

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout linear_back;

    private EditText edt_add;

    private Button btn_addfriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);


        initView();

    }

    private void initView() {

        linear_back = (LinearLayout) findViewById(R.id.information_linear_back);

        edt_add = (EditText) findViewById(R.id.edt_addfriend);
        btn_addfriend = (Button) findViewById(R.id.addfriend_btn_add);

        linear_back.setOnClickListener(this);

        btn_addfriend.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.information_linear_back:

                finish();
                break;
            case R.id.addfriend_btn_add:

                addFriend();
                break;
        }
    }

    private void addFriend() {

        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
                this);
        customProgressDialog.setMessage("正在添加...");
        customProgressDialog.show();

        String friendName = edt_add.getText().toString().trim();

        try {
            EMClient.getInstance().contactManager().addContact(friendName,"我是你的朋友");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    String fname = edt_add.getText().toString();
                    HttpUtils httpUtils = new HttpUtils();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("uid", MyApplication.userEntity.userData.getUid());
                    params.addBodyParameter("fname", fname);

                    httpUtils.send(HttpRequest.HttpMethod.POST, Config.ADDFD, params, new RequestCallBack<String>() {
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
                                        "添加成功",
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
                }
            }).start();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }
}
