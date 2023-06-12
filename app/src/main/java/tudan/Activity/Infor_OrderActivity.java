package tudan.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.EaseConstant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;
import com.wjx.homemaker.activity.MyChatActivity;
import com.wjx.homemaker.utils.APPConfig;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import tudan.Adapter.OrderAdapter;
import tudan.Bean.ReturnInfor;
import tudan.Bean.User;
import tudan.Item.GoodsItem;
import tudan.Tools.PhoneCallUtils;

public class Infor_OrderActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private SparseArray<GoodsItem> item;
    private OrderAdapter orderAdapter;
    private TextView user,chat,delivery_time,delivery_address,tvSubmit,tvCost,phone,content;
    private TextView call;
    private ReturnInfor returnInfor;
    private User user_infor;
    private NumberFormat nf;
    private int oid;
    private String uid;
    private double l,d;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==44){
                Toast.makeText(getApplicationContext(),"接单成功",Toast.LENGTH_LONG).show();
                Infor_OrderActivity.this.finish();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor__order);
        nf=NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        recyclerView= (RecyclerView) findViewById(R.id.selectRecyclerView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        item= MyApplication.getGet_selectItem();
        orderAdapter=new OrderAdapter(this,item);
        orderAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(orderAdapter);
        user= (TextView) findViewById(R.id.deliver_geUser);
        chat= (TextView) findViewById(R.id.btn_chat);
        delivery_time= (TextView) findViewById(R.id.deliver_getTime);
        delivery_address= (TextView) findViewById(R.id.deliver_getAddress);
        content= (TextView) findViewById(R.id.getContent);
        tvSubmit= (TextView) findViewById(R.id.tvSubmit);
        tvCost= (TextView) findViewById(R.id.tvCost);
        phone= (TextView) findViewById(R.id.phone);
        call= (TextView) findViewById(R.id.call);

        returnInfor=MyApplication.getReturnInfor();
        user_infor=returnInfor.getUser();
        user.setText(user_infor.getUsername());
        delivery_address.setText(returnInfor.getAddress());
        delivery_time.setText(returnInfor.getDelivery_time());
        phone.setText(user_infor.getPhone());
        System.out.println("总计:"+returnInfor.getTotalPrice());
        tvCost.setText(nf.format(returnInfor.getTotalPrice()));
        content.setText(returnInfor.getContent());
        oid=returnInfor.getOid();
        uid=user_infor.getUid();
        l=returnInfor.getL();
        d=returnInfor.getD();
        chat.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        call.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_chat:
                User user=new User();
                user=returnInfor.getUser();
                String name=MyApplication.userEntity.userData.getUsername();
                String photo= Config.BASEHOST+MyApplication.userEntity.userData.getAvatar();
                startChat(user.getUsername(),name,photo);
                break;
            case R.id.tvSubmit:
                tvSubmit();
                break;
            case R.id.call:
                PhoneCallUtils.call(user_infor.getPhone());

            default:
                break;
        }

    }
    public void tvSubmit(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils httpUtils=new HttpUtils();
                httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=getOrder&uid=" + MyApplication.userEntity.userData.getUid() + "&oid=" + oid+"&lon="+l+"&lng="+d, new RequestCallBack<Object>() {
                    @Override
                    public void onSuccess(ResponseInfo<Object> responseInfo) {
                        try {
                            JSONObject ret=new JSONObject(responseInfo.result.toString());
                            System.out.println("接单状态:"+responseInfo.result);
                            if (ret.getInt("errcode")==0){

                                Message message=new Message();
                            message.what=44;
                            handler.sendMessage(message);}
                            else {
                                System.out.println("接单失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        System.out.println("接单失败:"+s);
                    }
                });

            }
        }).start();
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

        MyApplication.getInstance().startActivity(intent);
    }
}
