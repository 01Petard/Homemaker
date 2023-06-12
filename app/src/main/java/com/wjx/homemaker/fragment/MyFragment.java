package com.wjx.homemaker.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.ContactListActivity;
import com.wjx.homemaker.activity.FreeActivity;
import com.wjx.homemaker.activity.InformationActivity;
import com.wjx.homemaker.activity.MainActivity;
import com.wjx.homemaker.activity.MyApplication;
import com.wjx.homemaker.activity.ShowInforActivity;
import com.wjx.homemaker.activity.VerifiedActivity;
import com.wjx.homemaker.adapter.MyGridAdapter;
import com.wjx.homemaker.entity.UserEntity;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.view.MyGridView;
import com.wjx.homemaker.view.XCRoundImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import tudan.Activity.ShoppingCartActivity;
import tudan.Tools.PhoneCallUtils;

/**
 * Created by admin on 2017/7/13.
 */

public class MyFragment extends Fragment {

    private MyGridView gridView;
    private MyGridView gridView2;

    private View rootView;

    private TextView tv_username;
    private TextView tv_phone;

    private XCRoundImageView img_username;

    private UserEntity userEntity;

    private LinearLayout my_ll_information;


    private String[] img_text = {"修改信息", "优惠券", "下单购买", "查看订单", "查看亲密度", "实名认证", "", ""};
    private int[] imgs = {R.mipmap.geren,
            R.mipmap.youhui, R.mipmap.goumai, R.mipmap.dingdan,
            R.mipmap.qingmi,
            R.mipmap.renzheng,
            R.mipmap.grid_bg,
            R.mipmap.grid_bg};

    private String[] img_text2 = {"电话咨询", "客服咨询", "更多", "邀请有礼"};
    private int[] imgs2 = {R.mipmap.dianhua,
            R.mipmap.gefu, R.mipmap.genduo, R.mipmap.yao};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_my, null);

        initView();

        return rootView;
    }

    private void initView() {

        my_ll_information = (LinearLayout) rootView.findViewById(R.id.my_ll_information);

        gridView = (MyGridView) rootView.findViewById(R.id.my_gridView);
        gridView2 = (MyGridView) rootView.findViewById(R.id.my_gridView2);

        tv_username = (TextView) rootView.findViewById(R.id.my_tv_username);
        tv_phone = (TextView) rootView.findViewById(R.id.my_tv_phone);

        img_username = (XCRoundImageView) rootView.findViewById(R.id.my_img_userImg);

        userEntity = MyApplication.userEntity;

        if (userEntity != null) {

            tv_username.setText(userEntity.userData.getNickname());
            tv_phone.setText(userEntity.userData.getPhone());

            Log.i("wjx", "initView: "+Config.BASEHOST+userEntity.userData.getAvatar() );

            new ImgDownTask().execute(Config.BASEHOST + userEntity.userData.getAvatar());


        }


        gridView.setAdapter(new MyGridAdapter(getActivity(), img_text, imgs));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:

                        Intent intent = new Intent(getActivity(), InformationActivity.class);

                        startActivityForResult(intent, i);


                        break;

                    case 1:

                        Intent intent1 = new Intent(getActivity(), FreeActivity.class);

                        startActivityForResult(intent1, i);

                        break;

                    case 2:

                        startActivity(new Intent(MyApplication.getInstance(), ShoppingCartActivity.class));

                        break;

                    case 3:

                        MainActivity mainActivity = (MainActivity) getActivity();

                        mainActivity.currentId = R.id.main_tv_order;
                        mainActivity.changeFragment(mainActivity.currentId);
                        mainActivity.changeSelect(mainActivity.currentId);

                        break;
                    case 4:

                        Intent intent4 = new Intent(getActivity(), ContactListActivity.class);

                        startActivityForResult(intent4, i);

                        break;
                    case 5:

                        Intent intent5 = new Intent(getActivity(), VerifiedActivity.class);

                        startActivityForResult(intent5, i);


                        break;
                }
            }
        });

        gridView2.setAdapter(new MyGridAdapter(getActivity(), img_text2, imgs2));

        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        PhoneCallUtils.call("18892621692");
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
            }
        });

        my_ll_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ShowInforActivity.class);

                startActivity(intent);

            }
        });
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        switch (requestCode) {
            case 0:

                Log.i("wjx", "startActivityForResult: ");

                break;
        }
    }

    public Bitmap getImageBitmap(String url) {
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
            img_username.setImageBitmap(bitmap);
        }
    }


}
