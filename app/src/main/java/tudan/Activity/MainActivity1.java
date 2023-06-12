package tudan.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wjx.homemaker.R;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import tudan.Bean.tudan;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener{
    private Button button1,button2,button3,button4,button5,button6;
    private String[] sort_item={"饮料酒水","休闲食品","服饰鞋包","糕点类","水果类","运动户外","日用百货","食品保健","礼品类","办公设备","家居家装","母婴用品","个护化妆","厨房用品","数码用品"};
    private String[] image_iten={"drink.png","food.png","clothes.png","cake.png","fruit.png","sports.png","materials.png","healthproduct.png","gift.png","official.png","home.png","babyproduct.png","cosmetic.png","kitchen.png","digitalproducts.png"};
    private String path= Environment.getExternalStorageDirectory().toString()+"/image_item/";
    private String url;
    private TextView textView;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.CALL_PHONE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Bmob.initialize(this,"cb2ec57701aad137383db96771a49fd3","bmob");
        verifyStoragePermissions(this);
        button1= (Button) findViewById(R.id.button1);
        button2= (Button) findViewById(R.id.button2);
        button3= (Button) findViewById(R.id.button3);
        button4=(Button)findViewById(R.id.button4);
        button5= (Button) findViewById(R.id.button5);
        button6= (Button) findViewById(R.id.button6);
        textView= (TextView) findViewById(R.id.textView);
        textView.setBackground(getDrawable(R.drawable.babyproduct));

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity1.this, ShoppingCartActivity.class));
            }
        });

//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                final ProgressDialog progressDialog=new ProgressDialog(MainActivity1.this);
////                progressDialog.setMessage("正在上传...");
////                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
////                progressDialog.show();
//                for (int i=0;i<image_iten.length;i++) {
//                    File file=new File(path+image_iten[i]);
//                    System.out.println(file);
//                    if (file!=null){
//                        System.out.println("查找成功"+image_iten[i]);
//                        final BmobFile bmobFile=new BmobFile(file);
//
//                        bmobFile.uploadblock(new UploadFileListener() {
//                            public void done(BmobException e) {
//                                if (e==null){
//                                    url=bmobFile.getUrl();
//
//                                    Toast.makeText(getApplicationContext(), "上传成功：" + url, Toast.LENGTH_LONG).show();
//                                }else
//                                    System.out.println("上传失败");
//                            }
//                        });
//
//                    }
//                    else
//                        System.out.println("文件为空");
//
//
//                }
////                progressDialog.dismiss();
//            }
//
//        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(path+image_iten[0]);
                final BmobFile file1=new BmobFile("矿泉水",null,file.toString());
                tudan goods=new tudan("1","饮料酒水");
                goods.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null)
                        {
                            System.out.println(s);
                            Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_LONG).show();
                        }else
                            System.out.println("添加失败:"+e.getMessage());
                    }
                });

            }
        });
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        BmobFile bmobFile=new BmobFile();
    }
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                PERMISSIONS_STORAGE[0]);
        int permission1 = ActivityCompat.checkSelfPermission(activity,
                PERMISSIONS_STORAGE[1]);
        int permission2 = ActivityCompat.checkSelfPermission(activity,
                PERMISSIONS_STORAGE[2]);
        int permission3 = ActivityCompat.checkSelfPermission(activity,
                PERMISSIONS_STORAGE[3]);
        int permission4 = ActivityCompat.checkSelfPermission(activity,
                PERMISSIONS_STORAGE[4]);

        if (permission != PackageManager.PERMISSION_GRANTED||permission1 != PackageManager.PERMISSION_GRANTED||permission2 != PackageManager.PERMISSION_GRANTED||permission3 != PackageManager.PERMISSION_GRANTED||permission4 != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button4:
                startActivity(new Intent(MainActivity1.this, NavigationActivity.class));
                break;
            case R.id.button5:
                startActivity(new Intent(MainActivity1.this, ExpressMapActivity.class));
                break;
            case R.id.button6:
                startActivity(new Intent(MainActivity1.this, Order_listActivity.class));
                break;
            default:
                break;
        }

    }
}
