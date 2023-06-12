package com.wjx.homemaker.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;
import com.wjx.homemaker.utils.Config;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tudan.Activity.RobActivity;
import tudan.Activity.ShoppingCartActivity;
import tudan.Adapter.InfoWinAdapter;
import tudan.Bean.ReturnInfor;
import tudan.Bean.User;
import tudan.Item.GoodsItem;

/**
 * Created by admin on 2017/7/13.
 */

public class ServiceFragment extends Fragment implements View.OnClickListener,LocationSource, AMapLocationListener, PoiSearch.OnPoiSearchListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener,AMap.OnMapClickListener,AMap.OnMarkerClickListener{

    private View rootView;

    private static final String TAG = "RobActivity";
    private MapView mMapView=null ;
    private AMap amap;
    private Marker mEndMarker;
    private Marker roundMarker;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private ImageView imageView;
    /**
     * 周边搜索条件
     */
    private PoiSearch.Query  query;
    /**
     * 周边搜索的业务执行
     */
    private PoiSearch poiSearch;
    /**
     * 逆地理编码业务类
     */
    private GeocodeSearch geocoderSearch;
    /**
     * 第一次定位的标志位
     */
    private boolean isFirstTime = true;
    private Context mContext;
    private List<ListViewHoldier> data = new ArrayList<>();
    //第一个位置数据，设为成员变量是因为有多个地方需要使用
    private ListViewHoldier lvHolder;
    private RecyclerView mRecyclerView;
    private CommonAdapter mAdapter;
    private View progressDialogView;
    private TextView tvHint;
    private TextView publish_order;
    private View progressbar;
    private Object object;
    private JSONArray jsonArray;
    private JSONObject jsonObject=null;
    private double l,d;
    private SparseArray<Marker> sparseArray=new SparseArray<>();
    private SparseArray<Marker> sparseArray1=new SparseArray<>();
    private SparseArray<SparseArray<GoodsItem>> sparseArraySparseArray=new SparseArray<>();
    private ArrayList<MarkerOptions> markerOptionses;
    private MarkerOptions markerOptions;
    private static LatLng endpoint;
    private Marker oldMarker;
    private InfoWinAdapter adapter;
    private SparseArray<GoodsItem> get_item;
    private SparseArray<ReturnInfor> returnInforSparseArray;
    private ReturnInfor returnInfor;



    Handler myhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==33)
            {
                object=msg.obj;
                get_item=new SparseArray<>();
                returnInforSparseArray=new SparseArray<>();
                try {
                    jsonArray=new JSONObject(object.toString()).getJSONArray("list");
                    if (mEndMarker!=null){
                        mEndMarker.remove();
                    }

//                    amap.clear(true);

//                    roundMarker=null;
                    // roundMarker.destroy();
                    for (int i=0;i<sparseArray.size();i++){
                        Marker marker=sparseArray.valueAt(i);
                        marker.remove();
                    }
                    if (roundMarker!=null){
                        roundMarker.remove();
                    }if ( oldMarker!=null){
                        oldMarker.showInfoWindow();
                        System.out.println("显示");
                    }
                    if (oldMarker==null){
                        System.out.println("空");

                    }

                    for (int i=0;i<jsonArray.length();i++){
                        jsonObject= (JSONObject) jsonArray.get(i);
                        JSONArray jsonArray1=jsonObject.getJSONArray("orderItemEntities");
                        for (int j=0;j<jsonArray1.length();j++){
                            JSONObject jsonObject2= (JSONObject) jsonArray1.get(j);
                            GoodsItem goodsItem=new GoodsItem();
                            goodsItem.price=jsonObject2.getDouble("price");
                            goodsItem.count=jsonObject2.getInt("number");
                            goodsItem.name=jsonObject2.getString("name");
                            get_item.put(j,goodsItem);
                        }

                        JSONObject user=jsonObject.getJSONObject("userEntity");
                        returnInfor=new ReturnInfor();
                        User user1=new User();
                        user1.setUsername(user.getString("username"));
                        user1.setUid(user.getString("uid"));
                        user1.setNickname(user.getString("nickname"));
                        user1.setSign(user.getString("sign"));
                        user1.setSex(user.getString("sex"));
                        user1.setBirthday(user.getString("birthday"));
                        user1.setHobby(user.getString("hobby"));
                        user1.setPhone(user.getString("phone"));
                        user1.setAvatar(user.getString("avatar"));
                        returnInfor.setUser(user1);
                        returnInfor.setDelivery_time(jsonObject.getString("endDate"));
                        returnInfor.setAddress(jsonObject.getString("address"));
                        returnInfor.setTotalPrice(jsonObject.getDouble("totalPrice"));
                        returnInfor.setOid(jsonObject.getInt("oid"));
                        returnInfor.setContent(jsonObject.getString("remarks"));
                        returnInfor.setTitle(jsonObject.getString("title"));


                        l=jsonObject.getDouble("l");
                        d=jsonObject.getDouble("d");
                        System.out.println("经纬度:"+l+","+d);

                        System.out.println("获取标记："+amap.getMapScreenMarkers());
                        LatLng roundLatLng = new LatLng(d,l);
                        int c= (int) ((l+d)*100000000);
                        sparseArraySparseArray.put(c,get_item);
                        returnInforSparseArray.put(c,returnInfor);
                        roundMarker=amap.addMarker(new MarkerOptions().position(roundLatLng)
                                .title(jsonObject.getString("title"))
                                .snippet(jsonObject.getString("remarks"))
                                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.userphoto))));
                        sparseArray.put(i,roundMarker);

//                        roundMarker.setPosition(roundLatLng);
//                        markerOptionses=new ArrayList<>();
//                        markerOptions=new MarkerOptions();
//                        markerOptions.position(roundLatLng);
//                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.roundpoint)));
//                        markerOptions.visible(true);
//                        markerOptionses.add(markerOptions);
                    }

//                    amap.addMarkers(markerOptionses,false);
                    mEndMarker = amap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.loc))));
                    mEndMarker.setPosition(endpoint);


//                    LatLng latLng=new LatLng(29.888954,121.481259);
//                    mEndMarker.setPosition(latLng);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_rob, null);

        initView();
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        initMap();
        return rootView;
    }

    private void initView() {
        mMapView = (MapView)rootView.findViewById(R.id.map);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.ll_rl_locations);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        publish_order= (TextView) rootView.findViewById(R.id.publish_order);
        progressDialogView = rootView.findViewById(R.id.ll_ll_holderview);
        imageView= (ImageView) rootView.findViewById(R.id.rl_iv_back1);
        tvHint = (TextView) rootView.findViewById(R.id.ll_tv_hint);
        progressbar = rootView.findViewById(R.id.ll_progressbar);
        imageView.setVisibility(View.GONE);
        publish_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ShoppingCartActivity.class));
            }
        });
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }

    /**
     * 地图实例化
     */
    private void initMap() {

        if (amap==null){
            amap = mMapView.getMap();}
        if (amap==null){
            System.out.println("空值");
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.strokeColor(Color.TRANSPARENT);//设置定位蓝点精度圆圈的边框颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);//设置定位蓝点精度
        amap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        System.out.println("0");
        amap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
        System.out.println("1");
        amap.getUiSettings().setMyLocationButtonEnabled(true); // 是否显示定位按钮
        amap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
        amap.moveCamera(CameraUpdateFactory.zoomTo(15));//设置地图缩放级别
        amap.setOnMapClickListener(this);
        amap.setOnMarkerClickListener(this);
        adapter=new InfoWinAdapter();
        amap.setInfoWindowAdapter(adapter);

        //圆圈的填充颜色
        System.out.println("2");

        System.out.println("2.5");
        lvHolder = new ListViewHoldier();
        System.out.println("3");
        //天添加屏幕移动的监听
        amap.setOnCameraChangeListener(this);
        System.out.println("4");
        // 初始化Marker添加到地图
        mEndMarker = amap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.loc))));

        System.out.println("5");
        //初始化 geocoderSearch
        geocoderSearch = new GeocodeSearch(getContext());
        //注册 逆地理编码异步处理回调接口
        geocoderSearch.setOnGeocodeSearchListener(this);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_btn_buy:
                startActivity(new Intent(MyApplication.getInstance(), ShoppingCartActivity.class));
                break;
            case R.id.service_btn_rob:
                startActivity(new Intent(MyApplication.getInstance(), RobActivity.class));
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    &&amapLocation.getErrorCode() == 0) {

                if(isFirstTime){//只要第一次的数据，当然，也可以在这里关闭定位
//                    mlocationClient.stopLocation();//停止定位
                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                    lvHolder.title = "[位置]";
                    lvHolder.address = amapLocation.getProvider()+amapLocation.getCity()+amapLocation.getStreet()+amapLocation.getStreetNum();
                    lvHolder.lp = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());
                    // mEndMarker.setPosition(new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude()));
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            getOrder(amapLocation.getLongitude(),amapLocation.getLatitude());
//
//                        }
//                    }).start();
                    System.out.println("当前位置:"+mEndMarker.getPosition());
                    data.add(0,lvHolder);
                    doSearchQuery();

                }

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    /**
     * 搜查周边数据
     */
    private void doSearchQuery() {
        //搜索类型
        String type = "汽车服务|汽车销售|" +
                "汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|" +
                "住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
                "金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";
        query = new PoiSearch.Query("", type, "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第一页

        poiSearch = new PoiSearch(getContext(), query);
        //搜索回调
        poiSearch.setOnPoiSearchListener(this);
        //搜索位置及范围
        poiSearch.setBound(new PoiSearch.SearchBound(lvHolder.lp, 1000));
        //同步搜索
//        poiSearch.searchPOI();//不能在主线程实现耗时操作
        //异步搜索
        poiSearch.searchPOIAsyn();

    }
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        mEndMarker.setPosition(cameraPosition.target);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

        //当地图定位成功的时候该方法也会回调，为了避免不必要的搜索，因此此处增加一个判断
        if(isFirstTime){
            isFirstTime = false;
            return;
        }
        //隐藏数据
        mRecyclerView.setVisibility(View.GONE);
        //展示dialogView
        progressDialogView.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.ll_progressbar).setVisibility(View.VISIBLE);
        tvHint.setText("加载中...");
        //marker 动画
        jumpPoint(mEndMarker);
        lvHolder.lp = new LatLonPoint(cameraPosition.target.latitude,cameraPosition.target.longitude);
        RegeocodeQuery query = new RegeocodeQuery(lvHolder.lp, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
        doSearchQuery();
    }
    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {


//        marker0.destroy();
        final Handler handler = new Handler();

        final long start = SystemClock.uptimeMillis();
        //获取地图投影坐标转换器
        Projection proj = amap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -50);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        endpoint=startLatLng;
        System.out.println("当前位置1:"+startLatLng);
//        PoiOrderAsyncTask poiOrderAsyncTask=new PoiOrderAsyncTask(myhandler);
//        poiOrderAsyncTask.execute(startLatLng.longitude,startLatLng.latitude);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getOrder(startLatLng.longitude,startLatLng.latitude);

            }
        }).start();

//        for (int i=0;i<sparseArray.size();i++){
//            com.fengliang.tudan.Tools.Point point0;
//            point0=sparseArray.valueAt(i);
//            LatLng roundLatLng = new LatLng(point0.getD(),point0.getL());
//
//            marker0.setPosition(roundLatLng);
//
//
//        }
        final long duration = 500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
//                System.out.println("位置:"+marker.getPosition());
                endpoint=new LatLng(lat,lng);

                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });

    }

    public void getOrder(Object... params){
        HttpUtils httpUtils=new HttpUtils();
        System.out.println("运行");

        try {

            httpUtils.send(HttpRequest.HttpMethod.POST, Config.BASEHOST+"/mobile/OrderServlet?method=getNearbyOrder&l="+params[0]+"&d="+params[1] , new RequestCallBack<Object>() {
                @Override
                public void onSuccess(ResponseInfo<Object> responseInfo) {
                    System.out.println("查找成功,返回订单："+responseInfo.result);
                    Message message=new Message();
                    message.what=33;
                    message.obj=responseInfo.result;
                    myhandler.sendMessage(message);




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


    }
    @Override
    public void onMapClick(LatLng latLng) {
        for (int i=0;i<sparseArray1.size();i++){
            Marker marker=sparseArray1.valueAt(i);
            marker.remove();
        }
        oldMarker.destroy();
//        amap.clear();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println("啦啦");
        if (!marker.getPosition().equals(endpoint)) {
            oldMarker=amap.addMarker(new MarkerOptions().position(marker.getPosition())
                    .title(marker.getTitle())
                    .snippet(marker.getSnippet())
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.userphoto))));

            int t= (int) ((marker.getPosition().latitude+marker.getPosition().longitude)*100000000);
            MyApplication.setGet_selectItem(sparseArraySparseArray.get(t));
            MyApplication.setReturnInfor(returnInforSparseArray.get(t));
            sparseArray1.put(t,oldMarker);
//            oldMarker.showInfoWindow();
            System.out.println("嘿嘿");
        }
        return false;
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(getContext());
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置为定位一次
//            mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位

        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                lvHolder.address = result.getRegeocodeAddress().getFormatAddress();
                data.remove(0);
                data.add(0,lvHolder);
            } else {
                //                ToastUtil.show(ReGeocoderActivity.this, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this, rCode);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    private CommonAdapter getAdapter() {
        return new CommonAdapter<ListViewHoldier>(getContext(),R.layout.item_listview_rob,data) {

            @Override
            protected void convert(ViewHolder holder, final ListViewHoldier listViewHoldier, int position) {
                holder.setText(R.id.rl_tv_name,listViewHoldier.title);
                holder.setText(R.id.rl_tv_location,listViewHoldier.address);

            }
        };
    }
    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItems != null && poiItems.size() > 0) {
                        for (int i = 0;i<poiItems.size();i++){
                            PoiItem poiitem = poiItems.get(i);
                            ListViewHoldier holder = new ListViewHoldier();
                            holder.address = poiitem.getSnippet();
                            holder.title = poiitem.getTitle();
                            holder.lp = poiitem.getLatLonPoint();

                            if(data.size()>i+1){
                                data.remove(i+1);
                            }
                            data.add(i+1,holder);

                        }

                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setVisibility(View.VISIBLE);
                        progressDialogView.setVisibility(View.GONE);

                    } else {
                        progressbar.setVisibility(View.GONE);
                        tvHint.setText("附近没有其它地点");
                    }
                }
            } else {
                progressbar.setVisibility(View.GONE);
                tvHint.setText("附近没有其它地点");
            }
        } else {
            progressbar.setVisibility(View.GONE);
            tvHint.setText("附近没有其它地点");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 周边数据实体封装
     */
    private class ListViewHoldier{
        String title;
        String address;
        LatLonPoint lp;
    }
}
