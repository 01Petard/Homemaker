package com.wjx.homemaker.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.R;
import com.wjx.homemaker.entity.FriendEntity;
import com.wjx.homemaker.fragment.CommunityFragment;
import com.wjx.homemaker.fragment.MyFragment;
import com.wjx.homemaker.fragment.OrderFragment;
import com.wjx.homemaker.fragment.ServiceFragment;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import tudan.Bean.User;

public class MainActivity extends AppCompatActivity {

    private ServiceFragment serviceFragment;
    private OrderFragment orderFragment;
    private CommunityFragment communityFragment;
    private MyFragment myFragment;

    private long exitTime=0;//两次按返回退出

    public int currentId = R.id.main_tv_community;

    private TextView tv_service;
    private TextView tv_order;
    private TextView tv_community;
    private TextView tv_my;
    private FragmentTransaction transaction;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==99){

//

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Bmob.initialize(this,"cb2ec57701aad137383db96771a49fd3","bmob");
        Bmob.initialize(this,"cb2ec57701aad137383db96771a49fd3","bmob");
        initView();
        initData();
    }

    private void initData() {

        final String url = Config.GETFD + MyApplication.userEntity.userData.getUid();

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUtils httpUtils = new HttpUtils();
                httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String json = responseInfo.result;
                        Log.i("wjx", "onSuccess: "+json);
                        FriendEntity friendEntity = GsonUtil.fromJson(json, FriendEntity.class);

                        if(friendEntity.errcode == 0){
                            MyApplication.friendEntity = friendEntity;
                            List<User> ulist = new ArrayList<User>();
                            for (int i = 0 ; i < friendEntity.list.size();i++) {
                                User user = new User();
                                user.setUsername(friendEntity.list.get(i).getUsername());
                                user.setUid(friendEntity.list.get(i).getUid());
                                ulist.add(new User());
                                ulist.add(user);
                            }
                            MyApplication.setUsers(ulist);
//                            Log.i("wjx", "onSuccess: "+MyApplication.friendEntity.list.get(0).getUsername());
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
    }


    private void initView() {
        tv_service = (TextView) findViewById(R.id.main_tv_service);
        tv_order = (TextView) findViewById(R.id.main_tv_order);
        tv_community = (TextView) findViewById(R.id.main_tv_community);
        tv_community.setSelected(true);
        tv_my = (TextView) findViewById(R.id.main_tv_my);

        communityFragment = new CommunityFragment();
        this.getSupportFragmentManager().beginTransaction().add(R.id.main_container, communityFragment).commit();

        tv_service.setOnClickListener(tabClickListener);
        tv_order.setOnClickListener(tabClickListener);
        tv_community.setOnClickListener(tabClickListener);
        tv_my.setOnClickListener(tabClickListener);

    }

    private View.OnClickListener tabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() != currentId) {//如果当前选中跟上次选中的一样,不需要处理
                changeSelect(v.getId());//改变图标跟文字颜色的选中
                changeFragment(v.getId());//fragment的切换
                currentId = v.getId();//设置选中id
            }
        }
    };

    public void changeSelect(int resId) {
        tv_service.setSelected(false);
        tv_order.setSelected(false);
        tv_community.setSelected(false);
        tv_my.setSelected(false);

        switch (resId) {
            case R.id.main_tv_service:
                tv_service.setSelected(true);
                break;
            case R.id.main_tv_order:
                tv_order.setSelected(true);
                break;
            case R.id.main_tv_community:
                tv_community.setSelected(true);
                break;
            case R.id.main_tv_my:
                tv_my.setSelected(true);
                break;
        }
    }

    public void changeFragment(int resId) {

        transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务

        hideFragments(transaction);//隐藏所有fragment
        if(resId==R.id.main_tv_service){//主页
            if(serviceFragment==null){//如果为空先添加进来.不为空直接显示
                serviceFragment = new ServiceFragment();
                transaction.add(R.id.main_container,serviceFragment);
            }else {
                transaction.show(serviceFragment);
            }
        }else if(resId==R.id.main_tv_order){//动态
            if(orderFragment==null){
                orderFragment = OrderFragment.newInstance(handler,this);
                transaction.add(R.id.main_container,orderFragment);
            }else {
                transaction.remove(orderFragment);
                orderFragment = OrderFragment.newInstance(handler,this);
                transaction.add(R.id.main_container,orderFragment);
//                transaction.show(orderFragment);
            }
        }else if(resId==R.id.main_tv_community){//消息中心
            if(communityFragment==null){
                communityFragment = new CommunityFragment();
                transaction.add(R.id.main_container,communityFragment);
            }else {
                transaction.show(communityFragment);
            }
        }else if(resId==R.id.main_tv_my){//我
            if(myFragment==null){
                myFragment = new MyFragment();
                transaction.add(R.id.main_container,myFragment);
            }else {
                transaction.show(myFragment);
            }
        }
        transaction.commit();//一定要记得提交事务
    }

    private void hideFragments(FragmentTransaction transaction){
        if (serviceFragment != null)//不为空才隐藏,如果不判断第一次会有空指针异常
            transaction.hide(serviceFragment);
        if (orderFragment != null)
            transaction.hide(orderFragment);
        if (communityFragment != null)
            transaction.hide(communityFragment);
        if (myFragment != null)
            transaction.hide(myFragment);
    }

    @Override
    public void onBackPressed() {


        exit();  ///退出应用

    }

    public void exit() {   //退出应用
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {

            EMClient.getInstance().logout(true);
            finish();
            //System.exit(0);
        }
    }
}
