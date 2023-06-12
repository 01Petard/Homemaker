package tudan.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;
import com.wjx.homemaker.entity.BaseEntity;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.GsonUtil;
import com.wjx.homemaker.view.XCRoundImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import tudan.Bean.ReturnInfor;
import tudan.Bean.User;

/**
 * Created by admin on 2017/7/24.
 */

public class UserInforActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_back;

    private XCRoundImageView img_user;

    private TextView tv_nickname;
    private TextView tv_sign;
    private TextView tv_username;
    private TextView tv_sex;
    private TextView tv_birth;
    private TextView tv_hobby;
    private TextView accept_order;
    private User user;
    private ReturnInfor returnInfor;

    private ImageView show_infor,imageview_notitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfor);

        initView();

    }

    private void initView() {

        ll_back = (LinearLayout) findViewById(R.id.show_back);

        img_user = (XCRoundImageView) findViewById(R.id.show_userimg);

        tv_nickname = (TextView) findViewById(R.id.show_tv_nickname);
        tv_sign = (TextView) findViewById(R.id.show_tv_sign);
        imageview_notitle= (ImageView) findViewById(R.id.imageview_notitle);
        accept_order= (TextView) findViewById(R.id.accept_order);
        tv_username = (TextView) findViewById(R.id.show_tv_username);
        tv_sex = (TextView) findViewById(R.id.show_tv_sex);
        tv_birth = (TextView) findViewById(R.id.show_tv_birth);
        tv_hobby = (TextView) findViewById(R.id.show_tv_hobby);

        show_infor = (ImageView) findViewById(R.id.show_infor);


//        new ImgDownTask().execute(Config.BASEHOST + MyApplication.userEntity.userData.getAvatar());
        returnInfor=MyApplication.getReturnInfor();
        user=returnInfor.getUser();
        tv_nickname.setText(user.getNickname());
        tv_sign.setText(user.getSign());
        tv_username.setText(user.getUsername());
        tv_sex.setText(user.getSex());
        tv_birth.setText(user.getBirthday());
        tv_hobby.setText(user.getHobby());
        show_infor.setVisibility(View.GONE);
        imageview_notitle.setVisibility(View.GONE);
        String avatar=user.getAvatar();
        new ImgDownTask().execute(Config.BASEHOST+avatar);

        ll_back.setOnClickListener(this);
        accept_order.setOnClickListener(this);
//        tv_sign.setOnClickListener(this);
//        show_infor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.show_back:

                finish();

                break;
            case R.id.accept_order:
                startActivity(new Intent(getApplicationContext(),Infor_OrderActivity.class));

//            case R.id.show_tv_sign:
//
//                infor_dialog();
//
//                break;

//            case R.id.show_infor:
//
//                Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
//
//                startActivity(intent);
//
//                break;
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
            img_user.setImageBitmap(bitmap);
        }
    }


    private void infor_dialog() {

        LinearLayout change_name = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.infor_dialog, null);
        TextView tv_name_dialog = (TextView) change_name.findViewById(R.id.tv_name_dialog);
        TextView tv_yname = (TextView) change_name.findViewById(R.id.dialog_yname);
        TextView tv_xname = (TextView) change_name.findViewById(R.id.dialog_xname);
        //由于EditText要在内部类中对其进行操作，所以要加上final
        final EditText et_name_dialog = (EditText) change_name.findViewById(R.id.et_name_dialog);


        tv_yname.setText("原个签:");
        tv_xname.setText("新个签:");
        tv_name_dialog.setText(tv_sign.getText().toString());
        et_name_dialog.setHint(tv_sign.getText().toString());

        new AlertDialog.Builder(UserInforActivity.this)
                .setTitle("修改个性签名")
                .setView(change_name)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tv_sign.setText(et_name_dialog.getText().toString());

                        final String s_sign = et_name_dialog.getText().toString();


                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                HttpUtils httpUtils = new HttpUtils();
                                RequestParams params = new RequestParams();
                                params.addBodyParameter("id", MyApplication.userEntity.userData.getUid());
                                params.addBodyParameter("sign", s_sign);

                                httpUtils.send(HttpRequest.HttpMethod.POST, Config.UPDATA, params, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        String json = responseInfo.result;

                                        Log.i("wjx", "onSuccess: " + json);
                                        BaseEntity baseEntity = GsonUtil.fromJson(json, BaseEntity.class);


                                        if (baseEntity.errcode == 0) {
                                            Toast.makeText(getApplicationContext(),
                                                    "修改成功",
                                                    Toast.LENGTH_LONG).show();
                                            MyApplication.userEntity.userData.setSign(s_sign);
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "修改失败:" + baseEntity.errmsg,
                                                    Toast.LENGTH_LONG).show();
                                            Log.i("wjx", baseEntity.errcode + "onSuccess: " + baseEntity.errmsg);
                                        }
                                    }

                                    @Override
                                    public void onFailure(HttpException exception, String msg) {
                                        Toast.makeText(getApplicationContext(),
                                                exception.getMessage() + " = " + msg,
                                                Toast.LENGTH_LONG).show();

                                        Log.i("wjx", exception + "onFailure: " + msg);

                                        return;
                                    }
                                });
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();

    }
}
