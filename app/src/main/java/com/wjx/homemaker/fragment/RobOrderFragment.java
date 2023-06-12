package com.wjx.homemaker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;
import com.wjx.homemaker.dialog.CustomProgressDialog;
import com.wjx.homemaker.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tudan.Adapter.Order_listAdapter;
import tudan.Bean.ReturnInfor;
import tudan.Bean.User;
import tudan.Item.GoodsItem;

/**
 * Created by admin on 2017/7/17.
 */

public class RobOrderFragment extends Fragment {


    private View rootView;
    private ListView oldorder=null;
    private SparseArray<GoodsItem> get_item;
    private SparseArray<ReturnInfor> returnInforSparseArray;
    private ReturnInfor returnInfor;
    private int count=0;
    private CustomProgressDialog customProgressDialog;
    private Activity activity;
    private  Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==98){
                request();
            }
        }
    };
    public static RobOrderFragment newInstance(Handler handler, Activity activity){
        RobOrderFragment robOrderFragment=new RobOrderFragment();
        robOrderFragment.activity=activity;
//        robOrderFragment.handler1=handler;
        return  robOrderFragment;
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==51){
                try {

                    returnInforSparseArray=new SparseArray<>();
                    JSONArray array= new JSONObject(msg.obj.toString()).getJSONArray("list");
                    for (int i=0;i<array.length();i++){
                        get_item=new SparseArray<>();
                        MyApplication.setCourier(true);

                        JSONObject object= (JSONObject) array.get(i);
                        returnInfor=new ReturnInfor();
                        if (object.getString("orderStatus")!=null){
                            System.out.println("是否付款");
                            returnInfor.setOrderStatus(object.getString("orderStatus"));
                            JSONObject logisticsEntity=object.getJSONObject("logisticsEntity");
                            if ((!logisticsEntity.getString("logisticsStatus").equals("FINISHED"))&&(!logisticsEntity.getString("logisticsStatus").equals("COMMENT"))){
                                System.out.println("未结束的");
                                returnInfor.setLogisticsStatus(logisticsEntity.getString("logisticsStatus"));

                                JSONArray jsonArray1=object.getJSONArray("orderItemEntities");
                                for (int j=0;j<jsonArray1.length();j++){
                                    JSONObject jsonObject2= (JSONObject) jsonArray1.get(j);
                                    GoodsItem goodsItem=new GoodsItem();
                                    goodsItem.price=jsonObject2.getDouble("price");
                                    goodsItem.count=jsonObject2.getInt("number");
                                    goodsItem.name=jsonObject2.getString("name");
                                    get_item.put(j,goodsItem);
                                }

                                JSONObject user=object.getJSONObject("userEntity");


                                User user1=new User();
                                user1.setUsername(user.getString("username"));
                                user1.setUid(user.getString("uid"));
                                user1.setPhone(user.getString("phone"));
                                user1.setAvatar(Config.BASEHOST+user.getString("avatar"));

                                user1.setNickname(user.getString("nickname"));
                                user1.setUserType(user.getString("userType"));
                                user1.setBirthday(user.getString("birthday"));
                                user1.setSign(user.getString("sign"));
                                user1.setTrack(user.getString("track"));
                                user1.setHobby(user.getString("hobby"));
                                user1.setSex(user.getString("sex"));

                                returnInfor.setGoodsItemSparseArray(get_item);
                                returnInfor.setUser(user1);
                                returnInfor.setDelivery_time(object.getString("endDate"));
                                returnInfor.setAddress(object.getString("address"));
                                returnInfor.setTotalPrice(object.getDouble("totalPrice"));
                                returnInfor.setOid(object.getInt("oid"));
                                MyApplication.getOid().put(i,object.getString("oid"));
                                returnInfor.setContent(object.getString("remarks"));
                                returnInfor.setTitle(object.getString("title"));
                                returnInforSparseArray.put(count,returnInfor);
                                count++;

                            } else {
                                System.out.println("已结束");
                            }
                        }else {
                            System.out.println("付款状态查询错误");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                init();
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rob, null);
        oldorder= (ListView) rootView.findViewById(R.id.list_roborder);
        request();


        return rootView;

    }
    private void  init(){
        List<Map<String,Object>> list=getData();
        Order_listAdapter adapter=new Order_listAdapter(MyApplication.getInstance(),list,returnInforSparseArray,handler1,activity);
        adapter.notifyDataSetChanged();
        oldorder.setAdapter(adapter);
    }
    private List<Map<String,Object>> getData(){
        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        for (int i=0;i<returnInforSparseArray.size();i++){
            ReturnInfor infor=new ReturnInfor();
            infor=returnInforSparseArray.get(i);
            User user=new User();
            user=infor.getUser();
            Map<String,Object> map= new HashMap<String,Object>();
            System.out.println("数据:"+infor.getTitle());
            map.put("title",infor.getTitle());
            map.put("username",user.getUsername());
            map.put("address",infor.getAddress());
            map.put("oid",infor.getOid());
            map.put("avatar",user.getAvatar());
            map.put("price",infor.getTotalPrice());
             if (infor.getLogisticsStatus().equals("START")){
                map.put("state","购物中接单人");
            }else if (infor.getLogisticsStatus().equals("SENDING")){
                map.put("state","配送中接单人");
            }
            list.add(map);


        }
        return list;
    }
    private void request(){
        count=0;
        customProgressDialog = new CustomProgressDialog(getContext());
        HttpUtils httpUtils=new HttpUtils();
        //需要用户ID
        customProgressDialog.setMessage("正在加载...");
        customProgressDialog.show();
        httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=getAllOrderByCourier&uid="+ MyApplication.userEntity.userData.getUid(), new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                try {
                    JSONObject ret=new JSONObject(responseInfo.result.toString());
                    System.out.println("获取用户订单数据:"+responseInfo.result);
                    if (ret.getInt("errcode")==0) {
                 if (customProgressDialog!=null&&customProgressDialog.isShowing()){
                        customProgressDialog.dismiss();
                    }
                    Toast.makeText(getContext(),"加载成功",Toast.LENGTH_LONG).show();
                        Message message=new Message();
                        message.what=51;
                        message.obj=responseInfo.result;
                        handler.sendMessage(message);
                    }
                    else {
                        if (customProgressDialog!=null&&customProgressDialog.isShowing()){
                            customProgressDialog.dismiss();
                        }
                        Toast.makeText(getContext(),"加载失败："+responseInfo.result,Toast.LENGTH_LONG).show();
                        System.out.println("获取失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("请求失败:"+s+" , "+e);


            }
        });

    }
}
