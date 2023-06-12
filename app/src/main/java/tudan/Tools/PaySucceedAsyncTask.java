package tudan.Tools;

import android.os.AsyncTask;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by fengliang
 * on 2017/7/10.
 */

public class PaySucceedAsyncTask extends AsyncTask<String,String,String> {

    @Override
    protected String doInBackground(String... params) {
        HttpUtils httpUtils = new HttpUtils();
        try {
            JSONObject object = new JSONObject();
            object.put("sn", params[0]);
            object.put("status", 1);

            System.out.println(object.toString());
            final String text = String.valueOf(object);
            String shuju= URLEncoder.encode(text.toString(),"utf-8");

            httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=updateStatus&&json="+shuju , new RequestCallBack<Object>() {
                @Override
                public void onSuccess(ResponseInfo<Object> responseInfo) {
                    try {
                        JSONObject jsonObject=new JSONObject(responseInfo.result.toString());
                        System.out.println(responseInfo.result);
                        if (jsonObject.getInt("errcode")==0)
                        System.out.println("支付成功，订单状态修改成功:"+responseInfo.result);
                        else
                            System.out.println("支付异常:"+responseInfo.result);
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
