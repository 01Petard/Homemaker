package tudan.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;
import com.wjx.homemaker.activity.MyChatActivity;
import com.wjx.homemaker.utils.APPConfig;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.SharedPreferencesUtils;

import java.text.NumberFormat;

import tudan.Adapter.OrderAdapter;
import tudan.Bean.ReturnInfor;
import tudan.Bean.User;
import tudan.Item.GoodsItem;
import tudan.Tools.PhoneCallUtils;

import static com.wjx.homemaker.activity.MyApplication.userEntity;

public class OneOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private SparseArray<GoodsItem> item;
    private TextView userText,phoneText;
    private OrderAdapter orderAdapter;
    private TextView user,chat,delivery_time,delivery_address,tvCost,phone,content;
    private TextView call;
    private ReturnInfor returnInfor;
    private User user_infor,courier;
    private NumberFormat nf;
    private int oid;
    private String uid;
    private double l,d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_order);
        getSupportActionBar().setTitle(MyApplication.getOneorder().getTitle());
        nf=NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        Intent intent=getIntent();

        recyclerView= (RecyclerView) findViewById(R.id.selectRecyclerView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        user= (TextView) findViewById(R.id.deliver_geUser);
        chat= (TextView) findViewById(R.id.btn_chat);
        userText= (TextView) findViewById(R.id.userText);
        phoneText= (TextView) findViewById(R.id.phoneText);
        delivery_time= (TextView) findViewById(R.id.deliver_getTime);
        delivery_address= (TextView) findViewById(R.id.deliver_getAddress);
        content= (TextView) findViewById(R.id.getContent);
        tvCost= (TextView) findViewById(R.id.tvCost);
        phone= (TextView) findViewById(R.id.phone);
        call= (TextView) findViewById(R.id.call);

        returnInfor=MyApplication.getOneorder();
        user_infor=returnInfor.getUser();
        courier=returnInfor.getCourier();
        user.setText(user_infor.getUsername());
        delivery_address.setText(returnInfor.getAddress());
        delivery_time.setText(returnInfor.getDelivery_time());
        item=returnInfor.getGoodsItemSparseArray();
        System.out.println("内容:"+item.size());
        orderAdapter=new OrderAdapter(this,item);
        orderAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(orderAdapter);
        phone.setText(user_infor.getPhone());
        System.out.println("总计:"+returnInfor.getTotalPrice());
        tvCost.setText(nf.format(returnInfor.getTotalPrice()));
        content.setText(returnInfor.getContent());
        oid=returnInfor.getOid();
        uid=user_infor.getUid();
        l=returnInfor.getL();
        d=returnInfor.getD();
        chat.setOnClickListener(this);
        call.setOnClickListener(this);
        if (intent!=null){
            int state=intent.getIntExtra("state",0);
            if (state==1){
                userText.setText("接单人:");
                user.setText(courier.getUsername());
                phoneText.setText("接单人电话:");
                phone.setText(courier.getPhone());

            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_chat:
                String userName = MyApplication.userEntity.userData.getUsername();
                String avatar = Config.BASEHOST + userEntity.userData.getAvatar();

                startChat(courier.getUsername(), avatar, userName);

                break;
            case R.id.call:
                PhoneCallUtils.call(phone.getText().toString());

            default:
                break;
        }

    }

    private void startChat(String userName,String avatar,String name) {

        //设置要发送出去的昵称
        SharedPreferencesUtils.setParam(this, APPConfig.USER_NAME, userName);
        //设置要发送出去的头像
        SharedPreferencesUtils.setParam(this, APPConfig.USER_HEAD_IMG, avatar);

        Intent intent = new Intent(OneOrderActivity.this, MyChatActivity.class);

        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, name);
        intent.putExtra("conversation", args);

        startActivity(intent);
    }
}
