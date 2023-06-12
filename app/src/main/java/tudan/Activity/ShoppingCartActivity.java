package tudan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;

import java.text.NumberFormat;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import tudan.Adapter.GoodsAdapter;
import tudan.Adapter.SelectAdapter;
import tudan.Adapter.TypeAdapter;
import tudan.Item.GoodsItem;
import tudan.Tools.DividerDecoration;



public class ShoppingCartActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imgcart;
    private ViewGroup anim_mask_layout;
    private RecyclerView rvtype,rvselected;
    private BottomSheetLayout bottomSheetLayout;
    private StickyListHeadersListView listView;
    private View bottomSheet;
    private NumberFormat nf;
    private Handler mHandler;
    private ArrayList<GoodsItem> dataList,typeList;
    private SparseArray<GoodsItem> selectedList;
    private SparseIntArray groupSelect;
    private TextView tvCount,tvCost,tvSubmit,tvTips;
    private TypeAdapter typeAdapter;
    private GoodsAdapter myAdapter;
    private SelectAdapter selectAdapter;
    public static Handler handler;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

            handler=new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    if (msg.what==11){
                        System.out.println("收到");
                        // dataList.clear();
                        dataList=GoodsItem.getGoodsList();
                        myAdapter=new GoodsAdapter(dataList,ShoppingCartActivity.this);
                        myAdapter.notifyDataSetChanged();
                        listView.setAdapter(myAdapter);

                        // myAdapter.notifyDataSetChanged();
                        // myAdapter=new GoodsAdapter(dataList,ShoppingCartActivity.this);
                    }
                    if (msg.what==12){
                        // myAdapter.notifyDataSetChanged();
                    }
                }
            };


            nf=NumberFormat.getCurrencyInstance();
            nf.setMaximumFractionDigits(2);
            mHandler=new Handler(getMainLooper());
            dataList=GoodsItem.getGoodsList();
            typeList=GoodsItem.getTypeList();
            GoodsItem.downbmob();
            selectedList=new SparseArray<>();
            groupSelect=new SparseIntArray();
            initView();



    }
    private  void initView(){
        tvCount= (TextView) findViewById(R.id.tvCount);
        tvCost= (TextView) findViewById(R.id.tvCost);
        tvTips= (TextView) findViewById(R.id.tvTips);
        tvSubmit= (TextView) findViewById(R.id.tvSubmit);
        rvtype= (RecyclerView) findViewById(R.id.typeRecyclerView);
        imgcart= (ImageView) findViewById(R.id.imgCart);
        anim_mask_layout= (ViewGroup) findViewById(R.id.containerLayout);
        bottomSheetLayout= (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);
        listView= (StickyListHeadersListView) findViewById(R.id.itemListView);
        rvtype.setLayoutManager(new LinearLayoutManager(this));
        typeAdapter=new TypeAdapter(this,typeList);
        rvtype.setAdapter(typeAdapter);
        typeAdapter.notifyDataSetChanged();
        rvtype.addItemDecoration(new DividerDecoration(this));
        myAdapter=new GoodsAdapter(dataList,this);
        myAdapter.notifyDataSetChanged();
        listView.setAdapter(myAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                GoodsItem item=dataList.get(firstVisibleItem);
                if (typeAdapter.selectTypeId!=item.typeId){
                    typeAdapter.selectTypeId=item.typeId;
                    typeAdapter.notifyDataSetChanged();
                    rvtype.smoothScrollToPosition(getSelectedGroupPosition(item.typeId));
                }
            }
        });

    }
    public void playAnimation(int [] start_location){
        ImageView img=new ImageView(this);
        img.setImageResource(R.drawable.button_add);
        setAnim(img,start_location);
    }
    private Animation createAnim(int startX,int startY){
        int[] des=new int[2];
        imgcart.getLocationInWindow(des);
        AnimationSet set=new AnimationSet(false);
        Animation translationX=new TranslateAnimation(0,des[0]-startX,0,0);
        translationX.setInterpolator(new LinearInterpolator());
        Animation translationY=new TranslateAnimation(0,0,0,des[1]-startY);
        translationY.setInterpolator(new AccelerateInterpolator());
        Animation alpha=new AlphaAnimation(1,0.5f);
        set.addAnimation(translationX);
        set.addAnimation(translationY);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }
    private void addViewToAnimLayout(final ViewGroup vg,final View view,int[] location){
        int x=location[0];
        int y=location[1];
        int[] loc=new int[2];
        vg.getLocationInWindow(loc);
        view.setX(x);
        view.setY(y-loc[1]);
        vg.addView(view);
    }
    private void setAnim(final View v,int[] start_location){
        addViewToAnimLayout(anim_mask_layout,v,start_location);
        Animation set=createAnim(start_location[0],start_location[1]);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anim_mask_layout.removeView(v);
                    }
                },100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(set);
    }

    //添加商品
    public void add(GoodsItem item ,boolean refreshGoodList){
        int groupCount=groupSelect.get(item.typeId);
        if (groupCount==0){
            groupSelect.append(item.typeId,1);
        }else {
            groupSelect.append(item.typeId,++groupCount);
        }
        GoodsItem temp=selectedList.get(item.id);
        if (temp==null){
            item.count=1;
            selectedList.append(item.id,item);
        }else {
            temp.count++;
        }
        update(refreshGoodList);
    }
    //移除商品
    public void remove(GoodsItem item,boolean refreshGoodList){
        int groupCount=groupSelect.get(item.typeId);
        if (groupCount==1){
            groupSelect.delete(item.typeId);
        }else if (groupCount>1){
            groupSelect.append(item.typeId,--groupCount);
        }
        GoodsItem temp=selectedList.get(item.id);
        if (temp!=null){
            if (temp.count<2){
                selectedList.remove(item.id);
            }else {
                item.count--;
            }
        }
        update(refreshGoodList);
    }

    //刷新布局，价格购买数量
    private void update(boolean refreshGoodList){
        int size=selectedList.size();
        int count=0;
        double cost=0;
        for (int i=0;i<size;i++){
            GoodsItem item=selectedList.valueAt(i);
            count+=item.count;
            cost+=item.count*item.price;
        }
        if (count<1){
            tvCount.setVisibility(View.GONE);
        }else{
            tvCount.setVisibility(View.VISIBLE);
        }
        tvCount.setText(String.valueOf(count));
        if (cost>0.00){
            tvTips.setVisibility(View.GONE);
            tvSubmit.setVisibility(View.VISIBLE);
        }else {
            tvSubmit.setVisibility(View.GONE);
            tvTips.setVisibility(View.VISIBLE);
        }

        tvCost.setText(nf.format(cost));
        MyApplication.setTotal(cost);
        if (myAdapter!=null && refreshGoodList){
            myAdapter.notifyDataSetChanged();
        }
        if(selectAdapter!=null){
            selectAdapter.notifyDataSetChanged();
        }
        if (typeAdapter!=null){
            typeAdapter.notifyDataSetChanged();
        }
        if (bottomSheetLayout.isSheetShowing() && selectedList.size()<1){
            bottomSheetLayout.dismissSheet();
        }
    }

    //清空购物车
    public void clearCart(){
        selectedList.clear();
        groupSelect.clear();
        update(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom:
                showBottomSheet();
                break;
            case R.id.clear:
                clearCart();
                break;
            case R.id.tvSubmit:
                Toast.makeText(ShoppingCartActivity.this, "结算", Toast.LENGTH_SHORT).show();
                MyApplication.setSelectedList(selectedList);
                Intent intent=new Intent(ShoppingCartActivity.this,OrderActivity.class);
//                Bundle bundle=new Bundle();
//                SparseParcelableArray sparseParcelableArray=new SparseParcelableArray();
//                for (int i=0;i<selectedList.size();i++){
//                    sparseParcelableArray.put(i+1,selectedList.get(i));
//                }
//                System.out.println("数量为:"+sparseParcelableArray.size());
//
//                bundle.putSparseParcelableArray("goods",sparseParcelableArray);
//                intent.putExtras(bundle);
                startActivityForResult(intent,1);

                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==100){
                clearCart();
            }
        }
    }

    //根据商品ID获取商品数量
    public int getSelectedItemCountById(int id){
        GoodsItem temp=selectedList.get(id);
        if (temp==null){
            return 0;
        }
        return temp.count;
    }
    //根据类别ID获取属于当前类别的数量
    public int getSelectedGroupByTypeId(int typeId){
        return groupSelect.get(typeId);
    }
    //根据类别ID获取分类的position用于滚动左侧的类别列表
    public int getSelectedGroupPosition(int typeId){
        for (int i=0;i<typeList.size();i++){
            if (typeId==typeList.get(i).typeId){
                return i;
            }
        }
        return 0;
    }
    public void onTypeClicked(int typeId){
        listView.setSelection(getSelectedPosition(typeId));
    }
    private int getSelectedPosition(int typeId){
        int position=0;
        for (int i=0;i<dataList.size();i++){
            if (dataList.get(i).typeId==typeId){
                position=i;
                break;
            }
        }
        return position;
    }
    private View createBottomSheetView(){
        View view= LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet, (ViewGroup) getWindow().getDecorView(),false);
        rvselected= (RecyclerView) view.findViewById(R.id.selectRecyclerView);
        rvselected.setLayoutManager(new LinearLayoutManager(this));
        TextView clear= (TextView) view.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        selectAdapter = new SelectAdapter(this,selectedList);
        rvselected.setAdapter(selectAdapter);
        return view;
    }
    private void showBottomSheet(){
        if(bottomSheet==null){
            bottomSheet = createBottomSheetView();
        }
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else {
            if(selectedList.size()!=0){
                bottomSheetLayout.showWithSheetView(bottomSheet);
            }
        }
    }
}
