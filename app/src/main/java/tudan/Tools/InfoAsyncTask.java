package tudan.Tools;

import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.activity.MyApplication;
import com.wjx.homemaker.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import tudan.Item.GoodsItem;

/**
 * Created by fengliang
 * on 2017/7/9.
 */

public class InfoAsyncTask  extends AsyncTask<Object,String,String> {

    @Override
    protected String doInBackground(Object... params) {
        HttpUtils httpUtils = new HttpUtils();

        SparseArray<GoodsItem> selectedList = MyApplication.getSelectedList();
      //  HttpPost request = new HttpPost("http://img.nook.one/mobile/OrderServlet?method=submit");
//        RequestParams params = new RequestParams();
        try {

            JSONObject object = new JSONObject();

            GoodsItem goodsItem;
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < selectedList.size(); i++) {
                System.out.println("值的个数：" + selectedList.size());
                JSONObject object2 = new JSONObject();
                goodsItem = selectedList.valueAt(i);
                System.out.println(goodsItem.toString());
                object2.put("name", goodsItem.name);
                object2.put("price", goodsItem.price);
                object2.put("number", goodsItem.count);
                jsonArray.put(object2);
            }

            object.put("orderItem", jsonArray);
            object.put("orderStatus", 0);
            object.put("serialNumber", params[0]);
            object.put("userid", MyApplication.userEntity.userData.getUid());
            String par1=params[1].toString();

            String part1 = par1.substring(2);

            Log.i("wjx", "doInBackground:" + part1);
            object.put("totalPrice", Double.valueOf(part1));
//            Log.i("wjx12345", "doInBackground:"+"哈哈"+ta.replaceAll(" +",""));
//            object.put("totalPrice", 2);
            object.put("remarks", params[2]);
            object.put("date",params[3]);
            object.put("address",params[4]);
            object.put("l",params[5]);
            object.put("d",params[6]);
            object.put("title",params[7]);

            System.out.println(object.toString());
            final String text = String.valueOf(object);
            String shuju= URLEncoder.encode(text.toString(),"utf-8");


//            params.addBodyParameter("json",text);
//            System.out.println(params);


//
//            params.addBodyParameter("orderItem",jsonArray.toString());
//            params.addBodyParameter("orderStatus",String.valueOf(0));
//            params.addBodyParameter("serialNumber",orderid);
//            params.addBodyParameter("userid",String.valueOf(1));
//            params.addBodyParameter("totalPrice",nf.format(Application.getTotal()));
//            params.addBodyParameter("remarks",content.getText().toString());
//            System.out.println(params.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=submit&&json="+shuju+"&robuid="+params[8] , new RequestCallBack<Object>(



        ) {
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                try {
                    JSONObject jsonObject=new JSONObject(responseInfo.result.toString());
                    System.out.println(responseInfo.result);
                    if (jsonObject.getInt("errcode")==0) {
                        System.out.println("发送成功:" + responseInfo.result);
                    }
                    else {
                        System.out.println("发送失败："+responseInfo.result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("发送失败:"+e);
            }
        });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
