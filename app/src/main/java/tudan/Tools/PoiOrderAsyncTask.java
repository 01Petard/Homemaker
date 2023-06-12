package tudan.Tools;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fengliang
 * on 2017/7/11.
 */

public class PoiOrderAsyncTask extends AsyncTask<Object,Object,Object> {
    private Handler handler;
    private Object obj;
    private int flag=1;

    public  PoiOrderAsyncTask(Handler handler){
        this.handler=handler;

    }
    @Override
    protected Object doInBackground(Object... params) {
        HttpUtils httpUtils=new HttpUtils();
        System.out.println("运行");

        try {

            httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=getNearbyOrder&l="+params[0]+"&d="+params[1] , new RequestCallBack<Object>() {
                @Override
                public void onSuccess(ResponseInfo<Object> responseInfo) {
                    System.out.println("查找成功,返回订单："+responseInfo.result);
                    obj =responseInfo.result;
                    flag=0;


                    try {
                        JSONArray object=new JSONObject(responseInfo.result.toString()).getJSONArray("list");

                        System.out.println("订单数据有："+object.length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        while (flag==1){

        }

        return obj;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Message messgae=handler.obtainMessage();
        messgae.what=33;
        messgae.obj=o;
        handler.sendMessage(messgae);

    }
}
