package com.wjx.homemaker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;


import com.wjx.homemaker.R;
import com.wjx.homemaker.utils.SharedUtils;


public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                if (SharedUtils.isFirstStart(getBaseContext())) {

                    startActivity(new Intent(getApplicationContext(), WelcomeGuideActivity.class));

                    SharedUtils.putIsFirstStart(getBaseContext(), false);
                } else {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                finish();
                return false;
            }
        }).sendEmptyMessageDelayed(0,1500);
    }
}
