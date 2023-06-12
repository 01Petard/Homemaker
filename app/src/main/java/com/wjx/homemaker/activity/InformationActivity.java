package com.wjx.homemaker.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.wjx.homemaker.view.XCRoundImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.wjx.homemaker.R.id.infor_tv_name;

/**
 * Created by admin on 2017/7/16.
 */

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linear_back;

    private XCRoundImageView information_img_userImg;

    private RelativeLayout rl_name;
    private RelativeLayout rl_phone;
    private RelativeLayout rl_sex;
    private RelativeLayout rl_hobby;
    private RelativeLayout rl_brith;
    private RelativeLayout rl_nickname;

    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_sex;
    private TextView tv_hobby;
    private TextView tv_brith;
    private TextView tv_nickname;

    private Button btn_updata;

    String path;
    int REQUEST_CODE = 1;
    int RESULT_PHOTO = 2;
    int ALL_PHOTO = 3;

    private final int DATE_DIALOG = 1;

    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);


        initView();

    }

    private void initView() {

        linear_back = (LinearLayout) findViewById(R.id.information_linear_back);
        information_img_userImg = (XCRoundImageView) findViewById(R.id.information_img_userImg);

        rl_name = (RelativeLayout) findViewById(R.id.infor_rl_name);
        rl_phone = (RelativeLayout) findViewById(R.id.infor_rl_phone);
        rl_sex = (RelativeLayout) findViewById(R.id.infor_rl_sex);
        rl_hobby = (RelativeLayout) findViewById(R.id.infor_rl_hobby);
        rl_brith = (RelativeLayout) findViewById(R.id.infor_rl_birth);
        rl_nickname = (RelativeLayout) findViewById(R.id.infor_rl_nickname);

        tv_name = (TextView) findViewById(infor_tv_name);
        tv_phone = (TextView) findViewById(R.id.infor_tv_phone);
        tv_sex = (TextView) findViewById(R.id.infor_tv_sex);
        tv_hobby = (TextView) findViewById(R.id.infor_tv_hobby);
        tv_brith = (TextView) findViewById(R.id.infor_tv_birth);
        tv_nickname = (TextView) findViewById(R.id.infor_tv_nickname);


        btn_updata = (Button) findViewById(R.id.infor_btn_updata);


        tv_name.setText(MyApplication.userEntity.userData.getUsername());
        tv_phone.setText(MyApplication.userEntity.userData.getPhone());


        tv_sex.setText(MyApplication.userEntity.userData.getSex());

        tv_hobby.setText(MyApplication.userEntity.userData.getHobby());

        tv_brith.setText(MyApplication.userEntity.userData.getBirthday());

        tv_nickname.setText(MyApplication.userEntity.userData.getNickname());

        String birth = MyApplication.userEntity.userData.getBirthday();


        Log.i("wjx", "initView: " + birth);
        Log.i("wjx", "initView: " + birth.substring(5,6));
        int index = 0;
        for (int i = 0 ; i < birth.length();i++) {
            if (birth.charAt(i) == '年') {
                mYear = Integer.parseInt(birth.substring(0, i));
                index = i;
            }
            if (birth.charAt(i) == '月') {
                mMonth = Integer.parseInt(birth.substring(index+1,i))-1;
                mDay = Integer.parseInt(birth.substring(i+1, birth.length()-1));
                break;
            }
        }

        new ImgDownTask().execute(Config.BASEHOST + MyApplication.userEntity.userData.getAvatar());
//        FinalBitmap bitmap = FinalBitmap.create(this);
//        url += MyAplication.user.getAvatar();
        //Log.i("url",url);
//        bitmap.display(information_img_userImg, Config.BASEHOST+MyApplication.userEntity.userData.getAvatar());

        information_img_userImg.setOnClickListener(new imageClick());
        linear_back.setOnClickListener(this);

        rl_name.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_hobby.setOnClickListener(this);
        rl_brith.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);

        btn_updata.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.information_linear_back:

                Intent intent = new Intent();
                setResult(0, intent);
                finish();
                break;
            case R.id.infor_rl_name:

                infor_dialog(0);
                break;
            case R.id.infor_rl_phone:
                infor_dialog(1);
                break;

            case R.id.infor_rl_sex:

                dialog_sex();

                break;
            case R.id.infor_rl_hobby:

                infor_dialog(2);

                break;
            case R.id.infor_rl_nickname:

                infor_dialog(3);

                break;
            case R.id.infor_rl_birth:

                Log.i("wjx", "onClick: " + mYear + ":" + mMonth + ":" + mDay);
                showDialog(DATE_DIALOG);


                break;
            case R.id.infor_btn_updata:

                doUpdata();

                break;
        }
    }

    private void doUpdata() {

        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
                this);
        customProgressDialog.setMessage("正在修改..");
        customProgressDialog.show();

        Bitmap userimg = ((BitmapDrawable) information_img_userImg.getDrawable()).getBitmap();
        final String s_userimg = Base64Util.bitmapToBase64(userimg);

        final String s_name = tv_name.getText().toString();
        final String s_phone = tv_phone.getText().toString();
        final String s_sex = tv_sex.getText().toString();
        final String s_hobby = tv_hobby.getText().toString();
        final String s_birth = tv_brith.getText().toString();
        final String s_nickname = tv_nickname.getText().toString();

        Log.i("wjx", "doUpdata: " + s_birth);
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUtils httpUtils = new HttpUtils();
                RequestParams params = new RequestParams();
                params.addBodyParameter("id", MyApplication.userEntity.userData.getUid());
                params.addBodyParameter("image", s_userimg);
                params.addBodyParameter("username", s_name);
                params.addBodyParameter("phone", s_phone);
                params.addBodyParameter("sex", s_sex);
                params.addBodyParameter("birthday", s_birth);
                params.addBodyParameter("hobby", s_hobby);
                params.addBodyParameter("nickname", s_nickname);

                httpUtils.send(HttpMethod.POST, Config.UPDATA, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String json = responseInfo.result;

                        Log.i("wjx", "onSuccess: " + json);
                        BaseEntity baseEntity = GsonUtil.fromJson(json, BaseEntity.class);

                        if (customProgressDialog != null
                                && customProgressDialog.isShowing()) {
                            customProgressDialog.dismiss();
                        }


                        if (baseEntity.errcode == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "上传成功",
                                    Toast.LENGTH_LONG).show();

                            MyApplication.userEntity.userData.setUsername(s_name);
                            MyApplication.userEntity.userData.setSex(s_sex);
                            MyApplication.userEntity.userData.setPhone(s_phone);
                            MyApplication.userEntity.userData.setHobby(s_hobby);
                            MyApplication.userEntity.userData.setBirthday(s_birth);
                            MyApplication.userEntity.userData.setNickname(s_nickname);


                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "上传失败:" + baseEntity.errmsg,
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        tv_brith.setText(new StringBuffer().append(mYear).append("年").append(mMonth+1).append("月").append(mDay).append("日"));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    private void dialog_sex() {
        LinearLayout change_name = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.infor_dialog_sex, null);

        TextView dialog_tv_sex = (TextView) change_name.findViewById(R.id.tv_sex_dialog);
        RadioGroup dialog_rg = (RadioGroup) change_name.findViewById(R.id.infor_rg);
        final RadioButton btn_man = (RadioButton) change_name.findViewById(R.id.rg_man);
        RadioButton btn_feman = (RadioButton) change_name.findViewById(R.id.rg_feman);


        dialog_tv_sex.setText(tv_sex.getText().toString());
        if (tv_sex.getText().toString().equals("男")) {
            btn_man.setChecked(true);
        } else {
            btn_feman.setChecked(true);
        }

        new AlertDialog.Builder(InformationActivity.this)
                .setTitle("修改性别")
                .setView(change_name)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (btn_man.isChecked()) {
                            tv_sex.setText("男");
                        } else {
                            tv_sex.setText("女");
                        }

                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    private void infor_dialog(final int choose) {

        LinearLayout change_name = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.infor_dialog, null);
        TextView tv_name_dialog = (TextView) change_name.findViewById(R.id.tv_name_dialog);
        TextView tv_yname = (TextView) change_name.findViewById(R.id.dialog_yname);
        TextView tv_xname = (TextView) change_name.findViewById(R.id.dialog_xname);
        //由于EditText要在内部类中对其进行操作，所以要加上final
        final EditText et_name_dialog = (EditText) change_name.findViewById(R.id.et_name_dialog);

        String type = "";
        if (choose == 0) {
            tv_name_dialog.setText(tv_name.getText().toString());
            et_name_dialog.setHint(tv_name.getText().toString());
            type = "用户名";
        } else if (choose == 1) {
            tv_yname.setText("原电话:");
            tv_xname.setText("新电话:");
            tv_name_dialog.setText(tv_phone.getText().toString());
            et_name_dialog.setHint(tv_phone.getText().toString());
            type = "电话";
        } else if (choose == 2) {
            tv_yname.setText("原兴趣:");
            tv_xname.setText("新兴趣:");
            tv_name_dialog.setText(tv_hobby.getText().toString());
            et_name_dialog.setHint(tv_hobby.getText().toString());
            type = "兴趣";
        } else if (choose == 3) {
            tv_yname.setText("原昵称:");
            tv_xname.setText("新昵称:");
            tv_name_dialog.setText(tv_nickname.getText().toString());
            et_name_dialog.setHint(tv_nickname.getText().toString());
            type = "昵称";
        }
        new AlertDialog.Builder(InformationActivity.this)
                .setTitle("修改" + type)
                .setView(change_name)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //将Activity中的textview显示AlertDialog中EditText中的内容
                        //并且用Toast显示一下
                        if (choose == 0) {
                            tv_name.setText(et_name_dialog.getText().toString());
                        } else if (choose == 1) {
                            tv_phone.setText(et_name_dialog.getText().toString());
                        } else if (choose == 2) {
                            tv_hobby.setText(et_name_dialog.getText().toString());
                        } else if (choose == 3) {
                            tv_nickname.setText(et_name_dialog.getText().toString());
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();

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
            information_img_userImg.setImageBitmap(bitmap);
        }
    }

    private class imageClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //PopupWindow----START-----这里开始到下面标记的地方是实现点击头像弹出PopupWindow，实现用户从PopupWindow中选择更换头像的方式
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
                    allPhoto();
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
                    startActivityForResult(intent, REQUEST_CODE);
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
            startPhoneZoom(Uri.fromFile(new File(path)));
        } else if (requestCode == REQUEST_CODE) {
            //相机返回结果，调用系统裁剪
            startPhoneZoom(Uri.fromFile(new File(path)));
        } else if (requestCode == RESULT_PHOTO) {
            //设置裁剪返回的位图
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = bundle.getParcelable("data");
                //将裁剪后得到的位图在组件中显示
                information_img_userImg.setImageBitmap(bitmap);
            }
        }
    }

    //调用系统裁剪的方法
    private void startPhoneZoom(Uri uri) {
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
        startActivityForResult(intent, RESULT_PHOTO);
    }

    //调用手机相册
    private void allPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ALL_PHOTO);
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

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
