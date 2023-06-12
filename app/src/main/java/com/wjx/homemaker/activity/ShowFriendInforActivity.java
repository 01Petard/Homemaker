package com.wjx.homemaker.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjx.homemaker.R;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.view.XCRoundImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by admin on 2017/7/24.
 */

public class ShowFriendInforActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_back;

    private XCRoundImageView img_user;

    private TextView tv_nickname;
    private TextView tv_sign;
    private TextView tv_username;
    private TextView tv_sex;
    private TextView tv_birth;
    private TextView tv_hobby;

    private ImageView show_infor;

    private int mposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriendinfor);

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

        new ImgDownTask().execute(Config.BASEHOST + MyApplication.friend_list.get(mposition).userData.getAvatar());

        tv_nickname.setText(MyApplication.friend_list.get(mposition).userData.getNickname());
        tv_sign.setText(MyApplication.friend_list.get(mposition).userData.getSign());
        tv_username.setText(MyApplication.friend_list.get(mposition).userData.getUsername());
        tv_sex.setText(MyApplication.friend_list.get(mposition).userData.getSex());
        tv_birth.setText(MyApplication.friend_list.get(mposition).userData.getBirthday());
        tv_hobby.setText(MyApplication.friend_list.get(mposition).userData.getHobby());


        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.show_back:

                finish();

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


}
