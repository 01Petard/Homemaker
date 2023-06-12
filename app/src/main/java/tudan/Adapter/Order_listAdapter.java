package tudan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import tudan.Activity.EvaluateActivity;
import tudan.Activity.ExpressMapActivity;
import tudan.Activity.NavigationActivity;
import tudan.Activity.OneOrderActivity;
import tudan.Activity.User_Show_InforActivity;
import tudan.Bean.ReturnInfor;

/**
 * Created by fengliang
 * on 2017/7/16.
 */

public class Order_listAdapter extends BaseAdapter implements View.OnClickListener {
    private List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private ImageView userphoto;
    private Context context;
    private TextView order_title,username,address,price,status,btn_evaluate,btn_order_guide,btn_order_buy,btn_order_place,btn_order_cancel,btn_payment,btn_confirm_received;
    private LinearLayout linearLay;
    private NumberFormat nf;
    private String state;
    private String oid;
    private  CustomProgressDialog customProgressDialog;
    private HttpUtils httpUtils;
    private SparseArray<ReturnInfor> returnInforSparseArray=new SparseArray<>();
    private Activity activity;
    private Handler handler1=new Handler();
    public Order_listAdapter(Context context,List<Map<String,Object>> data,SparseArray<ReturnInfor> returnInforSparseArray,Handler handler,Activity activity){
        this.context=context;
        this.data=data;
        this.layoutInflater=LayoutInflater.from(context);
        this.returnInforSparseArray=returnInforSparseArray;
        handler1=handler;
        this.activity=activity;
        customProgressDialog = new CustomProgressDialog(activity);

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (convertView==null) {
            convertView = layoutInflater.inflate(R.layout.order_list, null);
            nf = NumberFormat.getCurrencyInstance();
            nf.setMaximumFractionDigits(2);
            order_title = (TextView) convertView.findViewById(R.id.txt_business_name);
            username = (TextView) convertView.findViewById(R.id.username);
            userphoto = (ImageView) convertView.findViewById(R.id.userphoto);
            btn_evaluate = (TextView) convertView.findViewById(R.id.btn_evaluate);
            btn_confirm_received = (TextView) convertView.findViewById(R.id.btn_confirm_received);
            btn_payment = (TextView) convertView.findViewById(R.id.btn_payment);
            btn_order_cancel = (TextView) convertView.findViewById(R.id.btn_order_cancel);
            btn_order_place = (TextView) convertView.findViewById(R.id.btn_order_place);
            btn_order_buy = (TextView) convertView.findViewById(R.id.btn_order_buy);
            btn_order_guide = (TextView) convertView.findViewById(R.id.btn_order_guide);
            address = (TextView) convertView.findViewById(R.id.address);
            price = (TextView) convertView.findViewById(R.id.txt_total_price);
            status = (TextView) convertView.findViewById(R.id.txt_order_status);
            linearLay = (LinearLayout) convertView.findViewById(R.id.linearLay);
            userphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setUserInfor(returnInforSparseArray.get(position));
                    Intent intent = new Intent(context, User_Show_InforActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            linearLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setOneorder(returnInforSparseArray.get(position));
                    Intent intent = new Intent(context, OneOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
//        }
        order_title.setText((String) data.get(position).get("title")+"(订单标题)");
        username.setText("发起用户:"+(String) data.get(position).get("username"));
        address.setText("地址:"+(String) data.get(position).get("address"));
        price.setText("总价:"+nf.format(data.get(position).get("price")));
        String avatar= (String) data.get(position).get("avatar");
        String courieravater=(String) data.get(position).get("courieravater");
        new ImgDownTask().execute(avatar);

        if (data.get(position).get("state").equals("已完成")){
            status.setText("已完成");
            username.setText("接单人:"+(String) data.get(position).get("courierusername"));
            new ImgDownTask().execute(Config.BASEHOST+courieravater);
            btn_order_guide.setVisibility(View.GONE);
            btn_order_buy.setVisibility(View.GONE);
            btn_order_place.setVisibility(View.GONE);
            btn_order_cancel.setVisibility(View.GONE);
            btn_payment.setVisibility(View.GONE);
            btn_confirm_received.setVisibility(View.GONE);
            btn_evaluate.setVisibility(View.VISIBLE);
            linearLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setOneorder(returnInforSparseArray.get(position));
                    Intent intent=new Intent();
                    intent.putExtra("state",1);
                    intent.setClass(context,OneOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            userphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setUserInfor(returnInforSparseArray.get(position));
                    Intent intent=new Intent();
                    intent.putExtra("state",2);
                    intent.setClass(context, User_Show_InforActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }else if (data.get(position).get("state").equals("未付款")){
            status.setText("未付款");

            btn_order_guide.setVisibility(View.GONE);
            btn_order_buy.setVisibility(View.GONE);
            btn_order_place.setVisibility(View.GONE);
            btn_order_cancel.setVisibility(View.VISIBLE);
            btn_payment.setVisibility(View.VISIBLE);
            btn_confirm_received.setVisibility(View.GONE);
            btn_evaluate.setVisibility(View.GONE);
            linearLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setOneorder(returnInforSparseArray.get(position));
                    Intent intent = new Intent(context, OneOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }else if (data.get(position).get("state").equals("购物中")){
            status.setText("购物中");
                System.out.println("购物中123");
                username.setText("接单人:" + (String) data.get(position).get("courierusername"));
                new ImgDownTask().execute(Config.BASEHOST+courieravater);

            btn_order_guide.setVisibility(View.GONE);
            btn_order_buy.setVisibility(View.GONE);
            btn_order_place.setVisibility(View.VISIBLE);
            btn_order_cancel.setVisibility(View.GONE);
            btn_payment.setVisibility(View.GONE);
            btn_confirm_received.setVisibility(View.VISIBLE);
            btn_evaluate.setVisibility(View.GONE);
            linearLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setOneorder(returnInforSparseArray.get(position));
                    Intent intent=new Intent();
                    intent.putExtra("state",1);
                    intent.setClass(context,OneOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            userphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setUserInfor(returnInforSparseArray.get(position));
                    Intent intent=new Intent();
                    intent.putExtra("state",2);
                    intent.setClass(context, User_Show_InforActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }else if (data.get(position).get("state").equals("配送中")){
            status.setText("配送中");

                username.setText("接单人:" + (String) data.get(position).get("courierusername"));
                new ImgDownTask().execute(Config.BASEHOST+courieravater);

            btn_order_guide.setVisibility(View.GONE);
            btn_order_buy.setVisibility(View.GONE);
            btn_order_place.setVisibility(View.VISIBLE);
            btn_order_cancel.setVisibility(View.GONE);
            btn_payment.setVisibility(View.GONE);
            btn_confirm_received.setVisibility(View.VISIBLE);
            btn_evaluate.setVisibility(View.GONE);
            linearLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setOneorder(returnInforSparseArray.get(position));
                    Intent intent=new Intent();
                    intent.putExtra("state",1);
                    intent.setClass(context,OneOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            userphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setUserInfor(returnInforSparseArray.get(position));
                    Intent intent=new Intent();
                    intent.putExtra("state",2);
                    intent.setClass(context, User_Show_InforActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }else if (data.get(position).get("state").equals("购物中接单人")){
            status.setText("购物中");

            btn_order_guide.setVisibility(View.VISIBLE);
            btn_order_buy.setVisibility(View.VISIBLE);
            btn_order_place.setVisibility(View.GONE);
            btn_order_cancel.setVisibility(View.GONE);
            btn_payment.setVisibility(View.GONE);
            btn_confirm_received.setVisibility(View.GONE);
            btn_evaluate.setVisibility(View.GONE);
            linearLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setOneorder(returnInforSparseArray.get(position));
                    context.startActivity(new Intent(context, OneOrderActivity.class));
                }
            });
        }else if (data.get(position).get("state").equals("配送中接单人")){
            status.setText("配送中");
            btn_order_guide.setVisibility(View.VISIBLE);
            btn_order_buy.setVisibility(View.GONE);
            btn_order_place.setVisibility(View.GONE);
            btn_order_cancel.setVisibility(View.GONE);
            btn_payment.setVisibility(View.GONE);
            btn_confirm_received.setVisibility(View.VISIBLE);
            btn_evaluate.setVisibility(View.GONE);
            linearLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setOneorder(returnInforSparseArray.get(position));
                    context.startActivity(new Intent(context, OneOrderActivity.class));
                }
            });

        }else if (data.get(position).get("state").equals("已评价")){
            status.setText("已评价");
            username.setText("接单人:"+(String) data.get(position).get("courierusername"));
            new ImgDownTask().execute(Config.BASEHOST+courieravater);
            btn_order_guide.setVisibility(View.GONE);
            btn_order_buy.setVisibility(View.GONE);
            btn_order_place.setVisibility(View.GONE);
            btn_order_cancel.setVisibility(View.GONE);
            btn_payment.setVisibility(View.GONE);
            btn_confirm_received.setVisibility(View.GONE);
            btn_evaluate.setVisibility(View.GONE);
            linearLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setOneorder(returnInforSparseArray.get(position));
                    Intent intent=new Intent();
                    intent.putExtra("state",1);
                    intent.setClass(context,OneOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            userphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setUserInfor(returnInforSparseArray.get(position));
                    Intent intent=new Intent();
                    intent.putExtra("state",2);
                    intent.setClass(context, User_Show_InforActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }else if (data.get(position).get("state").equals("未接单")){
            status.setText("未接单");
            btn_order_guide.setVisibility(View.GONE);
            btn_order_buy.setVisibility(View.GONE);
            btn_order_place.setVisibility(View.GONE);
            btn_order_cancel.setVisibility(View.VISIBLE);
            btn_payment.setVisibility(View.GONE);
            btn_confirm_received.setVisibility(View.GONE);
            btn_evaluate.setVisibility(View.GONE);
            linearLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setOneorder(returnInforSparseArray.get(position));
                    Intent intent = new Intent(context, OneOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }else if (data.get(position).get("state").equals("已取消")){
            status.setText("已取消");
            btn_order_guide.setVisibility(View.GONE);
            btn_order_buy.setVisibility(View.GONE);
            btn_order_place.setVisibility(View.GONE);
            btn_order_cancel.setVisibility(View.GONE);
            btn_payment.setVisibility(View.GONE);
            btn_confirm_received.setVisibility(View.GONE);
            btn_evaluate.setVisibility(View.GONE);
            linearLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.setOneorder(returnInforSparseArray.get(position));
                    Intent intent = new Intent(context, OneOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        oid= data.get(position).get("oid").toString();
        httpUtils=new HttpUtils();
        btn_confirm_received.setOnClickListener(this);
        btn_evaluate.setOnClickListener(this);
        btn_order_buy.setOnClickListener(this);
        btn_order_cancel.setOnClickListener(this);
        btn_order_guide.setOnClickListener(this);
        btn_order_place.setOnClickListener(this);
        btn_payment.setOnClickListener(this);



        return convertView;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_order_cancel:
                customProgressDialog.setMessage("正在取消...");
                customProgressDialog.show();
                httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=cancel&oid=" + oid, new RequestCallBack<Object>() {
                    @Override
                    public void onSuccess(ResponseInfo<Object> responseInfo) {
                        System.out.println("取消订单状态:"+responseInfo.result);
                        try {

                            JSONObject ret=new JSONObject(responseInfo.result.toString());
                            if (ret.getInt("errcode")==0){
                                if (customProgressDialog!=null&&customProgressDialog.isShowing()){
                                    customProgressDialog.dismiss();
                                }
                                Toast.makeText(context,"取消订单成功",Toast.LENGTH_LONG).show();
                                Message msg=new Message();
                                msg.what=98;
                                handler1.sendMessage(msg);
                            }
                            else
                                Toast.makeText(context,"取消订单失败",Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        if (customProgressDialog!=null&&customProgressDialog.isShowing()){
                            customProgressDialog.dismiss();
                        }
                        Toast.makeText(context,"取消订单错误："+s,Toast.LENGTH_LONG).show();
                        System.out.println("取消订单状态:"+s);
                    }
                });
                break;
            case R.id.btn_confirm_received:
                customProgressDialog.setMessage("正在更新状态...");
                customProgressDialog.show();
                httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=finishOrderByCustomer&oid=" + oid + "&score=" + 0, new RequestCallBack<Object>() {
                    @Override
                    public void onSuccess(ResponseInfo<Object> reponseInfo) {

                        System.out.println("确认送达:"+reponseInfo.result);
                        try {

                            JSONObject ret=new JSONObject(reponseInfo.result.toString());
                            if (ret.getInt("errcode")==0){
                                if (customProgressDialog!=null&&customProgressDialog.isShowing()){
                                    customProgressDialog.dismiss();
                                }
                                Toast.makeText(context,"更新成功：",Toast.LENGTH_LONG).show();
                                Message msg=new Message();
                                msg.what=98;
                                handler1.sendMessage(msg);}
                            else
                                Toast.makeText(context,"更新失败",Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        if (customProgressDialog!=null&&customProgressDialog.isShowing()){
                            customProgressDialog.dismiss();
                        }
                        Toast.makeText(context,"更新状态错误："+s,Toast.LENGTH_LONG).show();
                        System.out.println("确认送达状态:"+s);

                    }
                });
                break;
            case R.id.btn_evaluate:
                //待改善
//                httpUtils.send(HttpRequest.HttpMethod.POST, "http://ali.nook.one/mobile/OrderServlet?method=finishOrderByCustomer&oid=" + oid + "&score=" + 5, new RequestCallBack<Object>() {
//                    @Override
//                    public void onSuccess(ResponseInfo<Object> reponseInfo) {
//                        System.out.println("评价状态:"+reponseInfo.result);
//
//                        Message msg=new Message();
//                        msg.what=98;
//                        handler1.sendMessage(msg);
//                    }
//
//                    @Override
//                    public void onFailure(HttpException e, String s) {
//                        System.out.println("评价状态:"+s);
//
//                    }
//                });
                Intent intent2=new Intent();
                intent2.putExtra("oid",oid);
                intent2.setClass(context, EvaluateActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);
                break;
            case R.id.btn_order_buy:
                customProgressDialog.setMessage("正在更新状态...");
                customProgressDialog.show();
                httpUtils.send(HttpRequest.HttpMethod.POST,Config.BASEHOST+"/mobile/OrderServlet?method=updateOrderLogistics&oid=" + oid, new RequestCallBack<Object>() {
                    @Override
                    public void onSuccess(ResponseInfo<Object> reponseInfo) {
                        System.out.println("更新状态为配送中:"+reponseInfo.result);
                        try {

                            JSONObject ret=new JSONObject(reponseInfo.result.toString());
                            if (ret.getInt("errcode")==0){
                                if (customProgressDialog!=null&&customProgressDialog.isShowing()){
                                    customProgressDialog.dismiss();
                                }
                                Toast.makeText(context,"更新成功：",Toast.LENGTH_LONG).show();
                                Message msg=new Message();
                                msg.what=98;
                                handler1.sendMessage(msg);}
                            else
                                Toast.makeText(context,"更新失败",Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                        System.out.println("更新状态为配送中:"+s);

                    }
                });

                break;
            case R.id.btn_order_guide:
                Intent intent=new Intent();
                intent.putExtra("oid",oid);
                intent.setClass(context,NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                break;
            case R.id.btn_order_place:
                Intent intent1=new Intent();
                intent1.putExtra("oid",oid);
                intent1.setClass(context,ExpressMapActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                break;
            case R.id.btn_payment:
                Toast.makeText(context,"请重新挑选商品",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }


    }
    class ImgDownTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            URL imgUrl = null;

            Bitmap bitmap = null;
            try {
                imgUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) imgUrl
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            userphoto.setImageBitmap(bitmap);
        }
    }
}
