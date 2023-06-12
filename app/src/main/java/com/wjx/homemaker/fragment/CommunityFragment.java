package com.wjx.homemaker.fragment;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjx.homemaker.R;
import com.wjx.homemaker.adapter.MessageGroupFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/13.
 */

public class CommunityFragment extends Fragment {

    private View rootView;

    private ViewPager vPager;

    private List<Fragment> list = new ArrayList<>();

    private ImageView ivShapeCircle;

    private TextView tv_chat;
    private TextView tv_circle;

    private MessageGroupFragmentAdapter adapter;

    private int offset = 0;
    private int currentIndex = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_community, null);

        initFragmnet();
        initCursorPosition();
        return rootView;
    }

    private void initCursorPosition() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        Matrix matrix = new Matrix();

        //标题栏我用weight设置权重  分成5份
        //(width / 5) * 2  这里表示标题栏两个控件的宽度
        //(width / 10)  标题栏一个控件的2分之一
        //7  约等于原点宽度的一半
        matrix.postTranslate((width / 4) * 2 + (width / 10)-5,0);//图片平移
        ivShapeCircle.setImageMatrix(matrix);

        //一个控件的宽度  我的手机宽度是1080/5=216 不同的手机宽度会不一样哦
        offset=(width / 4);
    }

    private void initFragmnet() {

        vPager = (ViewPager) rootView.findViewById(R.id.community_viewpager_home);


        ConversationListFragment conversationListFragment = new ConversationListFragment();
        CircleFragment circleFragment = new CircleFragment();

        list.add(conversationListFragment);
        list.add(circleFragment);

        adapter = new MessageGroupFragmentAdapter(getChildFragmentManager(), list);

        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(2);
        vPager.setCurrentItem(1);
        vPager.setOnPageChangeListener(pageChangeListener);

        ivShapeCircle = (ImageView) rootView.findViewById(R.id.community_iv_shape_circle);

        tv_chat = (TextView) rootView.findViewById(R.id.community_tv_chat);
        tv_circle = (TextView) rootView.findViewById(R.id.community_tv_circle);

        tv_circle.setSelected(true);

        tv_chat.setOnClickListener(clickListener);
        tv_circle.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.community_tv_chat:
                    //当我们设置setCurrentItem的时候就会触发viewpager的OnPageChangeListener借口,
                    //所以我们不需要去改变标题栏字体啥的
                    vPager.setCurrentItem(0);
                    break;
                case R.id.community_tv_circle:
                    vPager.setCurrentItem(1);
                    break;

            }
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int index) {
            changeTextColor(index);
            translateAnimation(index);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private void changeTextColor(int index){
        tv_chat.setSelected(false);
        tv_circle.setSelected(false);

        switch (index) {
            case 0:
                tv_chat.setSelected(true);
                break;
            case 1:
                tv_circle.setSelected(true);
                break;
        }
    }


    private void translateAnimation(int index){
        TranslateAnimation animation = null;
        switch(index){
            case 0:
                if(currentIndex==1){
                    animation=new TranslateAnimation(0,-offset,0,0);
                }
                break;
            case 1:
                if(currentIndex==0){
                    animation=new TranslateAnimation(-offset,0,0,0);
                }
                break;

        }
        animation.setFillAfter(true);
        animation.setDuration(300);
        ivShapeCircle.startAnimation(animation);

        currentIndex=index;
    }
}
