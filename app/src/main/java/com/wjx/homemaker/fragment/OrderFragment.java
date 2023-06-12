package com.wjx.homemaker.fragment;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
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

public class OrderFragment extends Fragment {

    private View rootView;

    private ViewPager vPager;

    private List<Fragment> list = new ArrayList<>();

    private ImageView ivShapeCircle;

    private TextView tv_old;
    private TextView tv_no;
    private TextView tv_rob;


    private MessageGroupFragmentAdapter adapter;

    private int offset = 0;
    private int currentIndex = 1;
    private static Handler handler1=new Handler();
    private  Activity activity;
    public  static OrderFragment newInstance(Handler handler, Activity activity){
        OrderFragment orderFragment=new OrderFragment();
        handler1=handler;
        orderFragment.activity=activity;
        return  orderFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order, null);
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
        matrix.postTranslate((width / 5) * 2 + (width / 10)-7,0);//图片平移
        ivShapeCircle.setImageMatrix(matrix);

        //一个控件的宽度  我的手机宽度是1080/5=216 不同的手机宽度会不一样哦
        offset=(width / 5);
    }

    private void initFragmnet() {

        vPager = (ViewPager) rootView.findViewById(R.id.viewpager_home);

        OldOrderFragment oldOrderFragment = OldOrderFragment.newInstance(handler1,activity);
        NoOrderFragment noOrderFragment = NoOrderFragment.newInstance(handler1,activity);
        RobOrderFragment robOrderFragment = RobOrderFragment.newInstance(handler1,activity);

        list.add(oldOrderFragment);
        list.add(noOrderFragment);
        list.add(robOrderFragment);

        adapter = new MessageGroupFragmentAdapter(getChildFragmentManager(), list);
        //adapter.notifyDataSetChanged();
        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(2);
        vPager.setCurrentItem(1);
        vPager.setOnPageChangeListener(pageChangeListener);

        ivShapeCircle = (ImageView) rootView.findViewById(R.id.iv_shape_circle);

        tv_old = (TextView) rootView.findViewById(R.id.order_tv_old);
        tv_no = (TextView) rootView.findViewById(R.id.order_tv_no);
        tv_rob = (TextView) rootView.findViewById(R.id.order_tv_rob);

        tv_no.setSelected(true);

        tv_old.setOnClickListener(clickListener);
        tv_no.setOnClickListener(clickListener);
        tv_rob.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.order_tv_old:
                    //当我们设置setCurrentItem的时候就会触发viewpager的OnPageChangeListener借口,
                    //所以我们不需要去改变标题栏字体啥的
                    vPager.setCurrentItem(0);
                    break;
                case R.id.order_tv_no:
                    vPager.setCurrentItem(1);
                    break;
                case R.id.order_tv_rob:
                    vPager.setCurrentItem(2);
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
        tv_old.setSelected(false);
        tv_no.setSelected(false);
        tv_rob.setSelected(false);

        switch (index) {
            case 0:
                tv_old.setSelected(true);
                break;
            case 1:
                tv_no.setSelected(true);
                break;
            case 2:
                tv_rob.setSelected(true);
                break;
        }
    }


    private void translateAnimation(int index){
        TranslateAnimation animation = null;
        switch(index){
            case 0:
                if(currentIndex==1){
                    animation=new TranslateAnimation(0,-offset,0,0);
                }else if (currentIndex == 2) {
                    animation = new TranslateAnimation(offset, -offset, 0, 0);
                }
                break;
            case 1:
                if(currentIndex==0){
                    animation=new TranslateAnimation(-offset,0,0,0);
                }else if(currentIndex==2){
                    animation=new TranslateAnimation(offset, 0,0,0);
                }
                break;
            case 2:
                if (currentIndex == 0) {
                    animation = new TranslateAnimation(-offset, offset, 0, 0);
                } else if(currentIndex==1){
                    animation=new TranslateAnimation(0,offset,0,0);
                }
                break;
        }
        animation.setFillAfter(true);
        animation.setDuration(300);
        ivShapeCircle.startAnimation(animation);

        currentIndex=index;
    }
}
