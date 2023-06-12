package com.wjx.homemaker.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wjx.homemaker.R;
import com.wjx.homemaker.dialog.CustomProgressDialog;
import com.wjx.homemaker.entity.BaseEntity;
import com.wjx.homemaker.utils.Base64Util;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.FileUitlity;
import com.wjx.homemaker.utils.GsonUtil;

import java.io.File;

/**
 * Created by admin on 2017/7/20.
 */

public class VerifiedActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linear_back;

    private EditText edt_name;
    private EditText edt_id;

    private ImageView img_front;
    private ImageView img_behind;

    private Button btn_submit;

    String path;
    int REQUEST_CODE = 1;
    int RESULT_PHOTO = 2;
    int ALL_PHOTO = 3;

    int REQUEST_CODE_2 = 4;
    int RESULT_PHOTO_2 = 5;
    int ALL_PHOTO_2 = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified);

        initView();
    }

    private void initView() {

        linear_back = (LinearLayout) findViewById(R.id.information_linear_back);

        edt_name = (EditText) findViewById(R.id.ve_edt_name);
        edt_id = (EditText) findViewById(R.id.ve_edt_id);

        img_front = (ImageView) findViewById(R.id.ve_img_front);
        img_behind = (ImageView) findViewById(R.id.ve_img_behind);

        btn_submit = (Button) findViewById(R.id.ve_btn_submit);

        img_front.setOnClickListener(new imageClick(true));

        img_behind.setOnClickListener(new imageClick(false));

        btn_submit.setOnClickListener(this);

        linear_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.information_linear_back:

                Intent intent = new Intent();
                setResult(5, intent);
                finish();
                break;

            case R.id.ve_btn_submit:

                doSubmit();
                break;
        }

    }

    private void doSubmit() {

        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
                this);
        customProgressDialog.setMessage("正在上传...");
        customProgressDialog.show();

        final String uid = MyApplication.userEntity.userData.getUid();
        final String realname = edt_name.getText().toString();
        final String idnum = edt_id.getText().toString();

        if (realname.equals("") || idnum.equals("")) {
            Toast.makeText(getApplicationContext(), "输入不能为空!", Toast.LENGTH_SHORT).show();
            if (customProgressDialog != null
                    && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();
            }
            return;
        }

        Bitmap img1 = ((BitmapDrawable) img_front.getDrawable()).getBitmap();

        Bitmap img2 = ((BitmapDrawable) img_behind.getDrawable()).getBitmap();

        final String front = Base64Util.bitmapToBase64(img1);

        final String behind = Base64Util.bitmapToBase64(img2);

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUtils httpUtils = new HttpUtils();
                RequestParams params = new RequestParams();
                params.addBodyParameter("uid", uid);
                params.addBodyParameter("idnum", idnum);
                params.addBodyParameter("realname", realname);
                params.addBodyParameter("front", front);
                params.addBodyParameter("behind", behind);
                httpUtils.send(HttpMethod.POST, Config.PUTUSER,params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String json = responseInfo.result;

                        Log.i("wjx", "onSuccess: " + json);
                        BaseEntity baseEntity = GsonUtil.fromJson(json, BaseEntity.class);

                        if (customProgressDialog != null
                                && customProgressDialog.isShowing()) {
                            customProgressDialog.dismiss();
                        }



                        if(baseEntity.errcode == 0){
                            Toast.makeText(getApplicationContext(),
                                    "上传成功",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "上传失败:"+baseEntity.errmsg,
                                    Toast.LENGTH_LONG).show();
                            Log.i("wjx", baseEntity.errcode+ "onSuccess: "+baseEntity.errmsg);
                        }
                    }

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        Toast.makeText(getApplicationContext(),
                                exception.getMessage() + " = " + msg,
                                Toast.LENGTH_LONG).show();

                        Log.i("wjx", exception+"onFailure: "+msg);
                        if (customProgressDialog != null
                                && customProgressDialog.isShowing()) {
                            customProgressDialog.dismiss();
                        }
                        return;
                    }
                });
            }
        }).start();

    }

    private class imageClick implements View.OnClickListener {

        Boolean isFirst;

        public imageClick(Boolean isFirst) {
            this.isFirst = isFirst;
        }

        @Override
        public void onClick(View v) {

            backgroundAlpha(0.3f);
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.popu_window, null);
            final PopupWindow popupWindow = new PopupWindow(view, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            //获取屏幕宽度
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            popupWindow.setWidth(dm.widthPixels);
            popupWindow.setAnimationStyle(R.style.popuwindow);
            //显示位置
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            popupWindow.setOnDismissListener(new poponDismissListener());

            //PopupWindow-----END
            //PopupWindow中对应的选择按钮
            TextView button = (TextView) view.findViewById(R.id.take_photo);//通过拍照的方式
            TextView button1 = (TextView) view.findViewById(R.id.all_photo);//通过相册的方式
            TextView button2 = (TextView) view.findViewById(R.id.out);//取消按钮

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //backgroundAlpha(1f);
                    popupWindow.dismiss();
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backgroundAlpha(1f);
                    popupWindow.dismiss();
                    //调用手机相册的方法,该方法在下面有具体实现
                    allPhoto(isFirst);
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backgroundAlpha(1f);
                    popupWindow.dismiss();
                    //调用手机照相机的方法,通过Intent调用系统相机完成拍照，并调用系统裁剪器裁剪照片
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //创建文件路径,头像保存的路径
                    File file = FileUitlity.getInstance(getApplicationContext()).makeDir("head_image");
                    //定义图片路径和名称
                    path = file.getParent() + File.separatorChar + System.currentTimeMillis() + ".jpg";
                    //保存图片到Intent中，并通过Intent将照片传给系统裁剪器
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
                    //图片质量
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    //启动有返回的Intent，即返回裁剪好的图片到RoundImageView组件中显示
                    if (isFirst) {
                        startActivityForResult(intent, REQUEST_CODE);
                    } else {
                        startActivityForResult(intent, REQUEST_CODE_2);
                    }
                }
            });
        }
    }

    //该方法实现通过何种方式跟换图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//如果返回码不为-1，则表示不成功
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == ALL_PHOTO) {
            //调用相册
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            //游标移到第一位，即从第一位开始读取
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
            //调用系统裁剪
            startPhoneZoom(Uri.fromFile(new File(path)),ALL_PHOTO);
        } else if (requestCode == REQUEST_CODE) {
            //相机返回结果，调用系统裁剪
            startPhoneZoom(Uri.fromFile(new File(path)),ALL_PHOTO);
        } else if (requestCode == RESULT_PHOTO) {
            //设置裁剪返回的位图
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = bundle.getParcelable("data");

                //将裁剪后得到的位图在组件中显示
                img_front.setImageBitmap(bitmap);
            }
        }

        if (requestCode == ALL_PHOTO_2) {
            //调用相册
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            //游标移到第一位，即从第一位开始读取
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
            //调用系统裁剪
            startPhoneZoom(Uri.fromFile(new File(path)),ALL_PHOTO_2);
        } else if (requestCode == REQUEST_CODE_2) {
            //相机返回结果，调用系统裁剪
            startPhoneZoom(Uri.fromFile(new File(path)),ALL_PHOTO_2);
        } else if (requestCode == RESULT_PHOTO_2) {
            //设置裁剪返回的位图
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = bundle.getParcelable("data");

                //将裁剪后得到的位图在组件中显示
                img_behind.setImageBitmap(bitmap);
            }
        }
    }

    //调用系统裁剪的方法
    private void startPhoneZoom(Uri uri,int ALL_PHOTO) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //是否可裁剪
        intent.putExtra("corp", "true");
        //裁剪器高宽比
        intent.putExtra("aspectY", 1);
        intent.putExtra("aspectX", 1);
        //设置裁剪框高宽
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //返回数据
        intent.putExtra("return-data", true);
        if (ALL_PHOTO == 3) {
            startActivityForResult(intent, RESULT_PHOTO);
        }
        if (ALL_PHOTO == 6) {
            startActivityForResult(intent, RESULT_PHOTO_2);
        }
    }

    //调用手机相册
    private void allPhoto(Boolean isFirst) {
        if (isFirst) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, ALL_PHOTO);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, ALL_PHOTO_2);
        }
    }

    /**
     * 添加PopupWindow关闭的事件，主要是为了将背景透明度改回来
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

}
