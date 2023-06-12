package tudan.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.ListView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.R;
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

public class Order_listActivity extends AppCompatActivity {
    private ListView listView=null;
    private SparseArray<GoodsItem> get_item;
    private SparseArray<ReturnInfor> returnInforSparseArray;
    private ReturnInfor returnInfor;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==55){
                try {
                    get_item=new SparseArray<>();
                    returnInforSparseArray=new SparseArray<>();
                    JSONArray array= new JSONObject(msg.obj.toString()).getJSONArray("list");
                    for (int i=0;i<array.length();i++){
                        JSONObject object= (JSONObject) array.get(i);
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
                        returnInfor.setOrderStatus(object.getString("orderStatus"));
                        JSONObject logisticsEntity=object.getJSONObject("logisticsEntity");
                        returnInfor.setLogisticsStatus(logisticsEntity.getString("logisticsStatus"));
                        returnInfor=new ReturnInfor();
                        User user1=new User();
                        user1.setUsername(user.getString("username"));
                        user1.setUid(user.getString("uid"));
                        user1.setPhone(user.getString("phone"));
                        returnInfor.setGoodsItemSparseArray(get_item);
                        returnInfor.setUser(user1);
                        returnInfor.setDelivery_time(object.getString("endDate"));
                        returnInfor.setAddress(object.getString("address"));
                        returnInfor.setTotalPrice(object.getDouble("totalPrice"));
                        returnInfor.setOid(object.getInt("oid"));
                        returnInfor.setContent(object.getString("remarks"));
                        returnInfor.setTitle(object.getString("title"));
                        returnInforSparseArray.put(i,returnInfor);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    init();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        listView= (ListView) findViewById(R.id.list_oder);
        request();

    }
    public void init(){
        List<Map<String,Object>>list=getData();
        Order_listAdapter adapter=new Order_listAdapter(this,list,returnInforSparseArray,null,null);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
    public List<Map<String,Object>> getData(){
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
            map.put("content",infor.getContent());
            map.put("price",infor.getTotalPrice());
            list.add(map);

        }
        return list;
    }
    public void request(){
        HttpUtils httpUtils=new HttpUtils();
        //需要用户ID
        httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=getAllOrderByUser&uid=1", new RequestCallBack<Object>() {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                try {
                    JSONObject ret=new JSONObject(responseInfo.result.toString());
                    System.out.println("获取用户订单数据:"+responseInfo.result);
                    if (ret.getInt("errcode")==0) {
//
                        Message message=new Message();
                        message.what=55;
                        message.obj=responseInfo.result;
                        handler.sendMessage(message);
                    }
                    else {
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
