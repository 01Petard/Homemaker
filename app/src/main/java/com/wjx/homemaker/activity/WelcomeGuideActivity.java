package com.wjx.homemaker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjx.homemaker.R;

import java.util.ArrayList;
import java.util.List;


public class WelcomeGuideActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_go;

    private ViewPager pager;

    private TextView tv_pass;

    private List<View> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);

        initView();

        initViewPager();


    }

    private void initViewPager() {

        list = new ArrayList<View>();

        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.mipmap.bg1);
        list.add(iv1);

        ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.mipmap.bg2);
        list.add(iv2);

        ImageView iv3 = new ImageView(this);
        iv3.setImageResource(R.mipmap.bg3);
        list.add(iv3);

        pager.setAdapter(new MyViewPagerAdapter());
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 2) {
                    btn_go.setVisibility(View.VISIBLE);
                    tv_pass.setVisibility(View.GONE);
                } else {
                    btn_go.setVisibility(View.GONE);
                    tv_pass.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initView() {

        btn_go = (Button) findViewById(R.id.welcome_guide_btn_go);
        pager = (ViewPager) findViewById(R.id.welcome_guide_viewpager);
        tv_pass = (TextView) findViewById(R.id.welcome_guide_tv_pass);

        tv_pass.setOnClickListener(this);
        btn_go.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.welcome_guide_btn_go:
            case  R.id.welcome_guide_tv_pass:

                startActivity(new Intent(this,LoginActivity.class));

                finish();

                break;
        }
    }

    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }

}
