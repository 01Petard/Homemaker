package com.wjx.homemaker.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.util.SparseArray;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.util.EMLog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.circle.MomentAdapter;
import com.wjx.homemaker.entity.CommunityEntity;
import com.wjx.homemaker.entity.FriendEntity;
import com.wjx.homemaker.entity.UserEntity;
import com.wjx.homemaker.helper.GlobalField;
import com.wjx.homemaker.helper.HxEaseuiHelper;
import com.wjx.homemaker.utils.Constant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tudan.Bean.ReturnInfor;
import tudan.Bean.User;
import tudan.Item.GoodsItem;

import static com.hyphenate.chat.EMGCMListenerService.TAG;


public class MyApplication extends MultiDexApplication implements AMapLocationListener {
    private static MyApplication instance;
    public static UserEntity userEntity = null;
    private static List<User> users =new ArrayList<User>();
    public static FriendEntity friendEntity = null;
    public static List<CommunityEntity.XX> friend_list;
    public static MomentAdapter mAdapter;
    public static com.wjx.homemaker.circle.User muser;


    private static String robuid;

    public static String getRobuid() {
        return robuid;
    }

    public static void setRobuid(String robuid) {
        MyApplication.robuid = robuid;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void setUsers(List<User> users) {
        MyApplication.users = users;
    }

    public static List<String> userNames = null;
    public static List<String> img_id = new ArrayList<>();
    private static double total;
    private static SparseArray<GoodsItem> get_selectItem;
    private static SparseArray<GoodsItem> selectedList;
    private static ReturnInfor returnInfor;
    private static Boolean courier=false;
    private AMapLocationClientOption mlocationOption=null;
    private AMapLocationClient mLocationClient;
    private static SparseArray<String> oid;

    private static double l;
    private static double d;

    private HttpUtils httpUtils;
    private String oidtest;
    private static ReturnInfor oneorder;
    private static ReturnInfor userInfor;

    public static ReturnInfor getUserInfor() {
        return userInfor;
    }

    public static void setUserInfor(ReturnInfor userInfor) {
        MyApplication.userInfor = userInfor;
    }

    public static SparseArray<String> getOid() {
        return oid;
    }

    public static void setOid(SparseArray<String> oid) {
        MyApplication.oid = oid;
    }


    public static ReturnInfor getOneorder() {
        return oneorder;
    }

    public static void setOneorder(ReturnInfor oneorder) {
        MyApplication.oneorder = oneorder;
    }



    public AMapLocationClientOption getMlocationOption() {
        return mlocationOption;
    }

    public void setMlocationOption(AMapLocationClientOption mlocationOption) {
        this.mlocationOption = mlocationOption;
    }

    public AMapLocationClient getmLocationClient() {
        return mLocationClient;
    }

    public void setmLocationClient(AMapLocationClient mLocationClient) {
        this.mLocationClient = mLocationClient;
    }

    public static double getL() {
        return l;
    }

    public static void setL(double l) {
        MyApplication.l = l;
    }

    public static double getD() {
        return d;
    }

    public static void setD(double d) {
        MyApplication.d = d;
    }

    public static Boolean getCourier() {
        return courier;
    }

    public static void setCourier(Boolean courier) {
        MyApplication.courier = courier;
    }

    public static ReturnInfor getReturnInfor() {
        return returnInfor;
    }

    public static void setReturnInfor(ReturnInfor returnInfor) {
        MyApplication.returnInfor = returnInfor;
    }

    public static SparseArray<GoodsItem> getGet_selectItem() {
        return get_selectItem;
    }

    public static void setGet_selectItem(SparseArray<GoodsItem> get_selectItem) {
        MyApplication.get_selectItem = get_selectItem;
    }

    public static double getTotal() {
        return total;
    }

    public static void setTotal(double total) {
        MyApplication.total = total;
    }

    public static SparseArray<GoodsItem> getSelectedList() {
        return selectedList;
    }

    public static void setSelectedList(SparseArray<GoodsItem> selectedList) {
        MyApplication.selectedList = selectedList;
    }





    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initHuanXin();

        EMOptions options = new EMOptions();
        options.setMipushConfig("2882303761517500800", "5371750035800");//小米推送的
        // 默认添加好友时，是不需要验证的，改成需要验证,true:自动验证,false,手动验证
        options.setAcceptInvitationAlways(true);
        //初始化
        EaseUI.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);
        EMClient.getInstance().updateCurrentUserNick(getSharedPreferences(GlobalField.USERINFO_FILENAME, MODE_PRIVATE).getString("username", "hdl"));//设置推送的昵称
        instance=this;
        mLocationClient=new AMapLocationClient(this);
        mlocationOption=new AMapLocationClientOption();
        mLocationClient.setLocationListener(this);
        mlocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mlocationOption.setInterval(10000);
        mLocationClient.setLocationOption(mlocationOption);
        mLocationClient.startLocation();
        httpUtils=new HttpUtils();
        oid=new SparseArray<>();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (courier){
                        for (int i=0;i<oid.size();i++){

                            oidtest=oid.valueAt(i);
                            System.out.println("定位信息："+oid.size()+" "+oidtest+" "+l+" "+d);
                        httpUtils.send(HttpRequest.HttpMethod.POST,  "http://de.by.cx:8080/kuaidi/mobile/LocationLogServlet?method=update&oid="+oidtest+"&lon="+l+"&lng="+d, new RequestCallBack<Object>() {
                            @Override
                            public void onSuccess(ResponseInfo<Object> responseInfo) {
                                try {
                                    JSONObject ret=new JSONObject(responseInfo.result.toString());
                                    System.out.println(responseInfo.result);
                                    if (ret.getInt("errcode")==0){
                                        System.out.println("定位成功");

                                    }
                                    else {
                                        System.out.println("定位失败");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {

                            }
                        });}
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    private void initHuanXin(){

        HxEaseuiHelper.getInstance().init(this.getApplicationContext());
        //设置全局监听
        setGlobalListeners();
    }


    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    EMConnectionListener connectionListener;

    /**
     * 设置一个全局的监听
     */
    protected void setGlobalListeners(){


        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {// 显示帐号已经被移除
                    onUserException(Constant.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {// 显示帐号在其他设备登录
                    onUserException(Constant.ACCOUNT_CONFLICT);
                    EMClient.getInstance().logout(true);//退出登录
                    Toast.makeText(getApplicationContext(),"退出成功",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {//
                    onUserException(Constant.ACCOUNT_FORBIDDEN);
                }
            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events

            }
        };

        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener);
    }

    /**
     * user met some exception: conflict, removed or forbidden
     */
    protected void onUserException(String exception){
        EMLog.e(TAG, "onUserException: " + exception);
        Toast.makeText(getApplicationContext(),exception,Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(getBaseContext(), UserQrCodeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(exception, true);
//        this.startActivity(intent);
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation!=null){
            if (aMapLocation.getErrorCode()==0){
                d=aMapLocation.getLatitude();
                l=aMapLocation.getLongitude();
                aMapLocation.getAccuracy();

            }
            else {
                System.out.println("Errcode:"+aMapLocation.getErrorInfo());
            }
        }

    }

}
