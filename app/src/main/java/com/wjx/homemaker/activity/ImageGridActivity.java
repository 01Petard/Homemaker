package com.wjx.homemaker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wjx.homemaker.R;
import com.wjx.homemaker.circle.AlbumHelper;
import com.wjx.homemaker.circle.ImageGridAdapter;
import com.wjx.homemaker.circle.ImageItem;
import com.wjx.homemaker.entity.ImageEntity;
import com.wjx.homemaker.helper.Bimp;
import com.wjx.homemaker.utils.Base64Util;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.GsonUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2017/7/18.
 */

public class ImageGridActivity extends Activity {

    public static final String EXTRA_IMAGE_LIST = "imagelist";
    List<ImageItem> dataList;
    GridView gridView;
    ImageGridAdapter adapter;
    AlbumHelper helper;
    Button bt;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ImageGridActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        dataList = (List<ImageItem>) getIntent().getSerializableExtra(
                EXTRA_IMAGE_LIST);

        initView();


        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = adapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext();) {
                    list.add(it.next());
                }

                if (Bimp.act_bool) {
                    Intent intent = new Intent(ImageGridActivity.this,
                            PublishedActivity.class);
                    startActivity(intent);
                    Bimp.act_bool = false;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < list.size(); i++) {
                            if (Bimp.drr.size() < 9) {
                                Bimp.drr.add(list.get(i));
                            }

                            String str = Bimp.drr.get(i);

                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(str);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap = BitmapFactory.decodeStream(fis);

                            String base = Base64Util.bitmapToBase64(bitmap);

                            HttpUtils httpUtils = new HttpUtils();
                            RequestParams params = new RequestParams();
                            params.addBodyParameter("image", base);
                            httpUtils.send(HttpMethod.POST, Config.UPNEW, params, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    String json = responseInfo.result;
                                    ImageEntity imageEntity = GsonUtil.fromJson(json, ImageEntity.class);
                                    Log.i("wjx", "onSuccess: " + imageEntity.aid);
                                    Log.i("wjx", "onSuccess: " + imageEntity.path);
                                    MyApplication.img_id.add(imageEntity.aid);
                                }

                                @Override
                                public void onFailure(HttpException exception, String msg) {
//                                    Toast.makeText(getApplicationContext(),
//                                            exception.getMessage() + " = " + msg,
//                                            Toast.LENGTH_LONG).show();
                                    Log.i("wjx", "onFailure: " + msg);
                                }
                            });
                        }
                    }
                }).start();

                finish();
            }

        });



    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
                mHandler);
        gridView.setAdapter(adapter);
        adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
            public void onListen(int count) {
                bt.setText("完成" + "(" + count + ")");
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // if(dataList.get(position).isSelected()){
                // dataList.get(position).setSelected(false);
                // }else{
                // dataList.get(position).setSelected(true);
                // }

                adapter.notifyDataSetChanged();
            }

        });

    }
}
