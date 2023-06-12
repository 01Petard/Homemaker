package tudan.Item;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

import com.wjx.homemaker.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import tudan.Activity.ShoppingCartActivity;
import tudan.Bean.Goods;

import static cn.bmob.v3.Bmob.getApplicationContext;


/**
 * Created by fengliang
 * on 2017/7/4.
 */

public class GoodsItem {
    public int id;
    public int typeId;
    public int rating;
    public String name;
    public String typeName;
    public double price;
    public int imagetype;
    public int count;
    public Bitmap img;
    private static ArrayList<GoodsItem> goodsList;
    private static ArrayList<GoodsItem> typeList;
    private static String[] goodsid=new String[10];
    private static String[] goodsprice=new String[10];
    private static String[] goodsname=new String[10];
    private static String[] goodstypename=new String[10];
    private static BmobFile[] goodsimage=new BmobFile[10];

    private static boolean state=true;
    private static boolean download_img=true;



    private static String[] sort_item={"饮料酒水","休闲食品","服饰鞋包","糕点类","水果类","运动户外","日用百货","食品保健","礼品类","办公设备","家居家装","母婴用品","个护化妆","厨房用品","数码用品"};
    private static int[] image_pic={R.drawable.drink,R.drawable.food,R.drawable.clothes,R.drawable.cake,R.drawable.fruit,R.drawable.sports,R.drawable.materials,R.drawable.healthproduct,R.drawable.gift,R.drawable.official,R.drawable.home,R.drawable.babyproduct,R.drawable.cosmetic,R.drawable.kitchen,R.drawable.digitalproducts};
    public GoodsItem(){}
    public GoodsItem(int id,double price,String name,int typeId,String typeName,int imagetype,Bitmap img ){
        this.id=id;
        this.price=price;
        this.name=name;
        this.typeId=typeId;
        this.typeName=typeName;
        this.imagetype=imagetype;
        this.img=img;
        rating=new Random().nextInt(5)+1;
    }

    private static void initData(){


        goodsList=new ArrayList<>();
        typeList=new ArrayList<>();
      //  File path= Application.getInstance().getFilesDir();
        String path_real=getApplicationContext().getCacheDir() + "/bmob/";

        GoodsItem item =null;
        System.out.println("哈哈哈");

        for (int i=1;i<16;i++){
            for (int  j=1;j<10;j++) {

                if (i==1&&goodsid[j-1]!=null){
//                    state_refresh=true;
                    System.out.println("改变");
                    state=false;
                    File file=new File(path_real,goodsname[j-1]+".jpg");
                    item=new GoodsItem(100*i+Integer.valueOf(goodsid[j-1]),Double.valueOf(goodsprice[j-1]),goodsname[j-1],i,sort_item[i-1],image_pic[i-1],getLoacalBitmap(file));
                }else {
                item = new GoodsItem(100 * i + j,Math.random()*100,"商品"+(100*i+j),i,sort_item[i-1], image_pic[i-1],null);}
                goodsList.add(item);
            }
            typeList.add(item);
        }
//        if (state_refresh){
//            Message message=new Message();
//            message.what=12;
//            ShoppingCartActivity shoppingCartActivity=new ShoppingCartActivity();
//            shoppingCartActivity.handler.sendMessage(message);
//        }
    }
    public static ArrayList<GoodsItem> getGoodsList(){

            initData();


        return goodsList;
    }
    public static ArrayList<GoodsItem> getTypeList(){
        if (typeList==null){
            initData();
        }
        return typeList;
    }
    public static Bitmap getLoacalBitmap(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//                Message message = new Message();
//                message.what = 11;
//                ShoppingCartActivity.handler.sendMessage(message);
            return null;
        }
    }
    public static void download(Boolean b) {
        if (b){
            for (int i = 0; i < goodsimage.length; i++) {
                    System.out.println("数量:"+goodsimage.length);
                if (goodsimage[i] != null) {
                    System.out.println(goodsimage[i].toString());

                    goodsimage[i].download(new DownloadFileListener() {

                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {

                                System.out.println("下载成功:" + s);
                                Message message = new Message();
                                message.what = 11;
                                ShoppingCartActivity shoppingCartActivity = new ShoppingCartActivity();
                                shoppingCartActivity.handler.sendMessage(message);

                            } else
                                System.out.println("下载失败:" + e.getMessage());
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {
                            Log.i("bmob", "下载进度:" + integer + "," + l);

                        }
                    });

                }
                else {
                    System.out.println("空了");
                }
             

            }
     download_img=false;}
    }
    public static void downbmob(){
        Bmob.initialize(getApplicationContext(),"cb2ec57701aad137383db96771a49fd3","bmob");
        BmobQuery<Goods> query=new BmobQuery<Goods>();
        query.addWhereEqualTo("sortname","饮料酒水");
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if (e==null){
                    int i=0;

//                    File path= Application.getInstance().getFilesDir();
//                    File file=new File(path.toString(),"tudan_img");
//                    if (!file.exists()){
//                        try {
//                            file.createNewFile();
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//                    }

                    for (Goods goods:list){
                        goodsid[i]=goods.getGoodsId();
                        goodsprice[i]=goods.getGoodsPrice();
                        goodsname[i]=goods.getGoodsname();
                        goodstypename[i]=goods.getSortname();
                        goodsimage[i]=goods.getGoodspic();

                        i++;
                    }
                    download(download_img);

                    System.out.println("成功");
                    if (state) {
                        Message message = new Message();
                        message.what = 11;
                        ShoppingCartActivity shoppingCartActivity = new ShoppingCartActivity();
                        shoppingCartActivity.handler.sendMessage(message);

                    }
                }else
                    System.out.println("失败:"+e.getMessage());
            }
        });
    }
}
