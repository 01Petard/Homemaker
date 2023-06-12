package com.wjx.homemaker.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.wjx.homemaker.R;

/**
 * Created by admin on 2017/8/2.
 */

public class QunActivity extends AppCompatActivity {

    private LinearLayout information_linear_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qun);
        initView();
    }

    private void initView() {

        information_linear_back = (LinearLayout) findViewById(R.id.information_linear_back);

        information_linear_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
