package com.wjx.homemaker.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.wjx.homemaker.R;
import com.wjx.homemaker.entity.FriendEntity;
import com.wjx.homemaker.utils.APPConfig;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.SharedPreferencesUtils;
import com.wjx.homemaker.view.XCRoundImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by admin on 2017/7/24.
 */

public class ShowFriendInfor2Activity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_back;

    private XCRoundImageView img_user;
    private FriendEntity friend = MyApplication.friendEntity;
    private TextView tv_nickname;
    private TextView tv_sign;
    private TextView tv_username;
    private TextView tv_sex;
    private TextView tv_birth;
    private TextView tv_hobby;

    private ImageView show_infor;

    private Button btn_chat;

    private int mposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriendinfor2);

        initData();
        initView();

    }

    private void initData() {

        Intent intent = getIntent();
        mposition = (int) intent.getExtras().getInt("position");
    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.show_back);

        img_user = (XCRoundImageView) findViewById(R.id.show_userimg2);

        tv_nickname = (TextView) findViewById(R.id.show_tv_nickname2);
        tv_sign = (TextView) findViewById(R.id.show_tv_sign2);
        tv_username = (TextView) findViewById(R.id.show_tv_username2);
        tv_sex = (TextView) findViewById(R.id.show_tv_sex2);
        tv_birth = (TextView) findViewById(R.id.show_tv_birth2);
        tv_hobby = (TextView) findViewById(R.id.show_tv_hobby2);

        btn_chat = (Button) findViewById(R.id.show_start_chat);

        new ImgDownTask().execute(Config.BASEHOST + friend.list.get(mposition).getAvatar());

        tv_nickname.setText(friend.list.get(mposition).getNickname());
        tv_sign.setText(friend.list.get(mposition).getSign());
        tv_username.setText(friend.list.get(mposition).getUsername());
        tv_sex.setText(friend.list.get(mposition).getSex());
        tv_birth.setText(friend.list.get(mposition).getBirthday());
        tv_hobby.setText(friend.list.get(mposition).getHobby());


        ll_back.setOnClickListener(this);

        btn_chat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.show_back:

                finish();

                break;

            case R.id.show_start_chat:

                startChat(friend.list.get(mposition).getUsername());

                break;
        }
    }


    class ImgDownTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            URL imgUrl = null;

            Bitmap bitmap = null;
            try {
                imgUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) imgUrl
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img_user.setImageBitmap(bitmap);
        }
    }

    private void startChat(String name) {

        //设置要发送出去的昵称
        SharedPreferencesUtils.setParam(this, APPConfig.USER_NAME, MyApplication.userEntity.userData.getUsername());
        //设置要发送出去的头像
        SharedPreferencesUtils.setParam(this, APPConfig.USER_HEAD_IMG, Config.BASEHOST + MyApplication.userEntity.userData.getAvatar());

        Intent intent = new Intent(ShowFriendInfor2Activity.this, MyChatActivity.class);

        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, name);
        intent.putExtra("conversation", args);

        startActivity(intent);
    }

}
