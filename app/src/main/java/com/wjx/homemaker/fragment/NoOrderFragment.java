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

public class NoOrderFragment extends Fragment {


    private View rootView;
    private ListView oldorder=null;
    private SparseArray<GoodsItem> get_item;
    private SparseArray<ReturnInfor> returnInforSparseArray;
    private ReturnInfor returnInfor;
    private String state;
    private int count=0;
    private Activity activity;
    private CustomProgressDialog customProgressDialog;
    private  Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==98){
            request();
            }
        }
    };
    public static NoOrderFragment newInstance(Handler handler, Activity activity){
        NoOrderFragment noOrderFragment=new NoOrderFragment();
        noOrderFragment.activity=activity;
//        noOrderFragment.handler1=handler;
        return  noOrderFragment;
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
                        JSONObject object= (JSONObject) array.get(i);
                        User courier=new User();
                        returnInfor=new ReturnInfor();
                        if (object.getString("orderStatus")!=null){
                            System.out.println("是否付款");
                            returnInfor.setOrderStatus(object.getString("orderStatus"));
                            JSONObject logisticsEntity=object.getJSONObject("logisticsEntity");
                            JSONObject jsonObject=logisticsEntity.getJSONObject("courier");

                            courier.setUsername(jsonObject.getString("username"));
                            courier.setNickname(jsonObject.getString("nickname"));
                            courier.setUserType(jsonObject.getString("userType"));
                            courier.setBirthday(jsonObject.getString("birthday"));
                            courier.setSign(jsonObject.getString("sign"));
                            courier.setTrack(jsonObject.getString("track"));
                            courier.setHobby(jsonObject.getString("hobby"));
                            courier.setPhone(jsonObject.getString("phone"));
                            courier.setUid(jsonObject.getString("uid"));
                            courier.setAvatar(jsonObject.getString("avatar"));
                            courier.setSex(jsonObject.getString("sex"));
                            if ((!logisticsEntity.getString("logisticsStatus").equals("FINISHED"))&&(!logisticsEntity.getString("logisticsStatus").equals("COMMENT"))){
                                System.out.println("未结束的");
                                returnInfor.setLogisticsStatus(logisticsEntity.getString("logisticsStatus"));
                                System.out.println("状态查询："+logisticsEntity.getString("logisticsStatus"));


                                JSONArray jsonArray1=object.getJSONArray("orderItemEntities");
                                for (int j=0;j<jsonArray1.length();j++){
                                    JSONObject jsonObject2= (JSONObject) jsonArray1.get(j);
                                    GoodsItem goodsItem=new GoodsItem();
                                    goodsItem.price=jsonObject2.getDouble("price");
                                    goodsItem.count=jsonObject2.getInt("number");
                                    goodsItem.name=jsonObject2.getString("name");
                                    get_item.put(j,goodsItem);
                                }
                                System.out.println("内有多少个："+get_item.size()+" "+jsonArray1.length());

                                JSONObject user=object.getJSONObject("userEntity");


                                User user1=new User();
                                user1.setUsername(user.getString("username"));
                                user1.setUid(user.getString("uid"));
                                user1.setAvatar(Config.BASEHOST+user.getString("avatar"));
                                user1.setPhone(user.getString("phone"));
                                user1.setNickname(user.getString("nickname"));
                                user1.setUserType(user.getString("userType"));
                                user1.setBirthday(user.getString("birthday"));
                                user1.setSign(user.getString("sign"));
                                user1.setTrack(user.getString("track"));
                                user1.setHobby(user.getString("hobby"));
                                user1.setSex(user.getString("sex"));
                                returnInfor.setGoodsItemSparseArray(get_item);
                                returnInfor.setUser(user1);
                                returnInfor.setCourier(courier);
                                returnInfor.setDelivery_time(object.getString("endDate"));
                                returnInfor.setAddress(object.getString("address"));
                                returnInfor.setTotalPrice(object.getDouble("totalPrice"));
                                returnInfor.setOid(object.getInt("oid"));
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
        rootView = inflater.inflate(R.layout.fragment_no, null);
        oldorder= (ListView) rootView.findViewById(R.id.list_noorder);
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
            System.out.println("大小:"+returnInforSparseArray.size());
            System.out.println("当前数:"+i);
            ReturnInfor infor=new ReturnInfor();
            infor=returnInforSparseArray.get(i);
            User user=new User();
            User courier=new User();
            courier=infor.getCourier();
            user=infor.getUser();
            Map<String,Object> map= new HashMap<String,Object>();
            System.out.println("数据:"+infor.getTitle());
            map.put("title",infor.getTitle());
            map.put("username",user.getUsername());
            map.put("courieravater",courier.getAvatar());
            map.put("courieruid",courier.getUid());
            map.put("courierusername",courier.getUsername());
            map.put("address",infor.getAddress());
            map.put("avatar",user.getAvatar());
            map.put("price",infor.getTotalPrice());
            map.put("oid",infor.getOid());
            if (infor.getOrderStatus().equals("UNPAID")){
                map.put("state","未付款");
            } else  if (infor.getOrderStatus().equals("CANCELED")){
                map.put("state","已取消");

            }else if (infor.getLogisticsStatus().equals("START")){
                map.put("state","购物中");
            }else if (infor.getLogisticsStatus().equals("SENDING")){
                map.put("state","配送中");
            }else if(infor.getLogisticsStatus().equals("NOTSTART")){
                map.put("state","未接单");
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
        httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=getAllOrderByUser&uid="+ MyApplication.userEntity.userData.getUid(), new RequestCallBack<Object>() {
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
