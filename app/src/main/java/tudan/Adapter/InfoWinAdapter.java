package tudan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.hyphenate.easeui.EaseConstant;
import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;
import com.wjx.homemaker.activity.MyChatActivity;
import com.wjx.homemaker.utils.APPConfig;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.SharedPreferencesUtils;

import tudan.Activity.UserInforActivity;
import tudan.Bean.ReturnInfor;
import tudan.Bean.User;


/**
 * Created by Teprinciple on 2016/8/23.
 * 地图上自定义的infowindow的适配器
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    private Context mContext = MyApplication.getInstance();
    private LatLng latLng;
    private LinearLayout order_accept;
    private LinearLayout chat;
    private TextView nameTV;
    private String agentName;
    private TextView addrTV;
    private String snippet;
    private ReturnInfor returnInfor;

    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        return view;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        snippet = marker.getSnippet();
        agentName = marker.getTitle();
    }

    @NonNull
    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.infor_order, null);
        chat = (LinearLayout) view.findViewById(R.id.chat);
        order_accept = (LinearLayout) view.findViewById(R.id.order_accept);
        nameTV = (TextView) view.findViewById(R.id.name);
        addrTV = (TextView) view.findViewById(R.id.addr);

        nameTV.setText(agentName);
        addrTV.setText(String.format("备注：%1$s",snippet));
        chat.setOnClickListener(this);
        order_accept.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.chat:  //点击聊天
//                NavigationUtils.Navigation(latLng);
                returnInfor=MyApplication.getReturnInfor();
                User user=new User();
                user=returnInfor.getUser();
                String name=MyApplication.userEntity.userData.getUsername();
                String photo= Config.BASEHOST+MyApplication.userEntity.userData.getAvatar();
                startChat(user.getUsername(),name,photo);


                break;

            case R.id.order_accept:  //点击接单

                Intent intent = new Intent(mContext, UserInforActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

                //Infor_OrderActivity
//                PhoneCallUtils.call("028-"); //TODO 处理电话号码
                break;
        }
    }
    private void startChat(String name,String userName,String avatar) {

        //设置要发送出去的昵称
        SharedPreferencesUtils.setParam(MyApplication.getInstance(), APPConfig.USER_NAME, userName);
        //设置要发送出去的头像
        SharedPreferencesUtils.setParam(MyApplication.getInstance(), APPConfig.USER_HEAD_IMG, avatar);

        Intent intent = new Intent(MyApplication.getInstance(), MyChatActivity.class);

        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, name);
        intent.putExtra("conversation", args);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getInstance().startActivity(intent);
    }
}
