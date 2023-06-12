package tudan.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amap.api.services.help.Tip;
import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import tudan.Adapter.FriendsAdapter;
import tudan.Adapter.OrderAdapter;
import tudan.Bean.User;
import tudan.Item.GoodsItem;
import tudan.Tools.InfoAsyncTask;
import tudan.Tools.PaySucceedAsyncTask;

public class OrderActivity extends AppCompatActivity {
    private SparseArray<GoodsItem> selectedList;
    private RecyclerView rvselected;
    private OrderAdapter orderAdapter;
    private TextView tvcost,delivery_time,delivery_address,tvSubmit;
    private Calendar showTime;
    private NumberFormat nf;
    private int hour,minute,year,month,day;
    private String orderid,delivery_date,now;
    private SimpleDateFormat s;
    private EditText content,detailed_address,order_title;
    private URL url;
    private Date date;
    private long l;
    private String robuid;
    private double longitude;
    private Spinner spinner;
    private List<User> users;
    private double latitude;

    // 此为测试Appid,请将Appid改成你自己的Bmob AppId
    String APPID = "204507cd57ac8f70da94cb1c8fcee497";
    // 此为微信支付插件的官方最新版本号,请在更新时留意更新说明
    int PLUGINVERSION = 7;

    EditText name, price, body, order;
    Button go;

    ProgressDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        nf=NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        // 初始化BmobPay对象,可以在支付时再初始化
        BP.init(APPID);
        rvselected= (RecyclerView) findViewById(R.id.selectRecyclerView);
        EventBus.getDefault().register(this);
        delivery_time= (TextView) findViewById(R.id.delivery_time);
        delivery_address= (TextView) findViewById(R.id.delivery_address);
        content= (EditText) findViewById(R.id.edid_task_content);
        detailed_address= (EditText) findViewById(R.id.detailed_address);
        order_title= (EditText) findViewById(R.id.order_title);
        tvSubmit= (TextView) findViewById(R.id.tvSubmit);
        spinner= (Spinner) findViewById(R.id.spinner);
        users=new ArrayList<User>();

        users=MyApplication.getUsers();
//        //Test此处的赋值只为了测试
//        List<User> list=new ArrayList<User>();
//        list.add(new User());
//        list.add(new User("土蛋",1));
//        list.add(new User("叶敏杰",2));
//        list.add(new User("王晶旭",3));
//        list.add(new User("冯亮",4));
//
//        //--------------
        FriendsAdapter friendsAdapter=new FriendsAdapter(this,users);//把list改成users
        spinner.setAdapter(friendsAdapter);

        tvcost= (TextView) findViewById(R.id.tvCost);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvselected.setLayoutManager(layoutManager);
        selectedList= MyApplication.getSelectedList();
        showTime=Calendar.getInstance();
        s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        now=s.format(showTime.getTime());

        orderAdapter = new OrderAdapter(this,selectedList);
        orderAdapter.notifyDataSetChanged();
        rvselected.setAdapter(orderAdapter);
        tvcost.setText(nf.format(MyApplication.getTotal()));
        year=showTime.get(Calendar.YEAR);
        month=showTime.get(Calendar.MONTH)+1;
        day=showTime.get(Calendar.DATE);
        hour=showTime.get(Calendar.HOUR_OF_DAY);

        minute=showTime.get(Calendar.MINUTE);
        System.out.println("时间:"+year+"-"+month+"-"+day+" "+hour+" "+minute);
        delivery_time.setText(hour+":"+minute);
        delivery_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDatePicker();
            }
        });
        delivery_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this,PoiSearchActivity.class);//拾取坐标点
                startActivity(intent);

            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(true);
                System.out.println("支付中.....");
//

            }
        });

    }
    /**
     * 获取终点信息
     * @param tip
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Tip tip) {
        delivery_address.setText(tip.getDistrict()+tip.getName());
        longitude=tip.getPoint().getLongitude();
        latitude=tip.getPoint().getLatitude();
        System.out.println(latitude+"  "+longitude);


    };
    /**
     * 时间选择器
     *
     */
    public void onDatePicker() {
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                showTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                showTime.set(Calendar.MINUTE,minute);
                delivery_time.setText(DateFormat.format("HH:mm",showTime));
            }
        },showTime.get(Calendar.HOUR_OF_DAY),showTime.get(Calendar.MINUTE),true).show();
    }
    private void installApk(String s) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
        } else {
            installBmobPayPlugin(s);
        }
    }
    // 执行订单查询
    void query() {
        showDialog("正在查询订单...");
        final String orderId = getOrder();

        BP.query(orderId, new QListener() {

            @Override
            public void succeed(String status) {
                Toast.makeText(OrderActivity.this, "查询成功!该订单状态为 : " + status,
                        Toast.LENGTH_SHORT).show();
                hideDialog();
            }

            @Override
            public void fail(int code, String reason) {
                Toast.makeText(OrderActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    // 默认为0.02
    double getPrice() {
        double price = MyApplication.getTotal();
//        try {
//            price = Double.parseDouble(this.price.getText().toString());
//        } catch (NumberFormatException e) {
//        }
        return price;
    }

    // 商品详情(可不填)
    String getName() {
        return this.name.getText().toString();
    }

    // 商品详情(可不填)
    String getBody() {
        return this.body.getText().toString();
    }

    // 支付订单号(查询时必填)
    String getOrder() {
        return this.order.getText().toString();
    }

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    /**
     * 安装assets里的apk文件
     *
     * @param fileName
     */
    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTPERMISSION) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    installBmobPayPlugin("bp.db");
                } else {
                    //提示没有权限，安装不了
                    Toast.makeText(OrderActivity.this,"您拒绝了权限，这样无法安装支付插件",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    /**
     * 检查某包名应用是否已经安装
     *
     * @param packageName 包名
     * @param browserUrl  如果没有应用市场，去官网下载
     * @return
     */
    private boolean checkPackageInstalled(String packageName, String browserUrl) {
        try {
            // 检查是否有支付宝客户端
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有安装支付宝，跳转到应用市场
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + packageName));
                startActivity(intent);
            } catch (Exception ee) {// 连应用市场都没有，用浏览器去支付宝官网下载
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(browserUrl));
                    startActivity(intent);
                } catch (Exception eee) {
                    Toast.makeText(OrderActivity.this,
                            "您的手机上没有没有应用市场也没有浏览器，我也是醉了，你去想办法安装支付宝/微信吧",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }
    private static final int REQUESTPERMISSION = 101;
    /**
     * 调用支付
     *
     * @param alipayOrWechatPay 支付类型，true为支付宝支付,false为微信支付
     */
    void pay(final boolean alipayOrWechatPay) {
        if (alipayOrWechatPay) {
            if (!checkPackageInstalled("com.eg.android.AlipayGphone",
                    "https://www.alipay.com")) { // 支付宝支付要求用户已经安装支付宝客户端
                Toast.makeText(OrderActivity.this, "请安装支付宝客户端", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        } else {
            if (checkPackageInstalled("com.tencent.mm", "http://weixin.qq.com")) {// 需要用微信支付时，要安装微信客户端，然后需要插件
                // 有微信客户端，看看有无微信支付插件，170602更新了插件，这里可检查可不检查
                if (!BP.isAppUpToDate(this, "cn.bmob.knowledge", 8)){
                    Toast.makeText(
                            OrderActivity.this,
                            "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)",
                            Toast.LENGTH_SHORT).show();
                    installApk("bp.db");
                    return;
                }
            } else {// 没有安装微信
                Toast.makeText(OrderActivity.this, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        showDialog("正在获取订单...\nSDK版本号:" + BP.getPaySdkVersion());
        final String name = "test";

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            this.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        BP.pay(name, "111", getPrice(), alipayOrWechatPay, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(OrderActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(OrderActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                System.out.println("支付成功");
                PaySucceedAsyncTask paySucceedAsyncTask=new PaySucceedAsyncTask();
                paySucceedAsyncTask.execute(orderid);
                hideDialog();
                startActivity(new Intent(OrderActivity.this,PaySucceedActivity.class));
                setResult(100);
                OrderActivity.this.finish();

            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                orderid=orderId;
                delivery_date=year+"/"+month+"/"+day+" "+delivery_time.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
                try {
                    date=sdf.parse(delivery_date);
                     l=date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                InfoAsyncTask task=new InfoAsyncTask();
                task.execute(orderid,nf.format(MyApplication.getTotal()),content.getText().toString(),l,delivery_address.getText().toString()+" "+detailed_address.getText().toString(),longitude,latitude,order_title.getText().toString(),robuid);
                order.setText(orderId);
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            OrderActivity.this,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
//                    installBmobPayPlugin("bp.db");
                    installApk("bp.db");
                } else {
                    Toast.makeText(OrderActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                }

                hideDialog();
            }
        });
    }
}
