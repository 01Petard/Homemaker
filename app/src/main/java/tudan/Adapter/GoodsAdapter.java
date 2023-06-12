package tudan.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wjx.homemaker.R;

import java.text.NumberFormat;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import tudan.Activity.ShoppingCartActivity;
import tudan.Item.GoodsItem;


/**
 * Created by fengliang
 * on 2017/7/4.
 */

public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter{
    private ArrayList<GoodsItem> dataList;
    private ShoppingCartActivity mContext;
    private NumberFormat nf;
    private LayoutInflater mInflater;

    public GoodsAdapter(ArrayList<GoodsItem> dataList,ShoppingCartActivity mContext){
        this.dataList=dataList;
        this.mContext=mContext;
            nf= NumberFormat.getCurrencyInstance();
            nf.setMaximumFractionDigits(2);
            mInflater=LayoutInflater.from(mContext);

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.item_header_view,parent,false);
        }
        ((TextView)(convertView)).setText(dataList.get(position).typeName);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return dataList.get(position).typeId;
    }

    @Override
    public int getCount() {
        if (dataList==null){
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder=null;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.item_goods,parent,false);
            holder=new ItemViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ItemViewHolder) convertView.getTag();
        }
        GoodsItem item=dataList.get(position);
        holder.bindData(item);
        return  convertView;
    }
    class ItemViewHolder implements View.OnClickListener{
        private TextView name,price,tvadd,tvminus,tvcount;
        private GoodsItem item;
        private ImageView img;
        private RatingBar ratingBar;
        public ItemViewHolder(View itemView){
            name= (TextView) itemView.findViewById(R.id.tvName);
            price= (TextView) itemView.findViewById(R.id.tvPrice);
            tvcount= (TextView) itemView.findViewById(R.id.count);
            tvminus= (TextView) itemView.findViewById(R.id.tvMinus);
            tvadd= (TextView) itemView.findViewById(R.id.tvAdd);
            img= (ImageView) itemView.findViewById(R.id.img);
            ratingBar= (RatingBar) itemView.findViewById(R.id.ratingBar);
            tvminus.setOnClickListener(this);
            tvadd.setOnClickListener(this);
        }
        public void bindData(GoodsItem item){
            this.item=item;
            name.setText(item.name);
            ratingBar.setRating(item.rating);



            img.setImageBitmap(item.img);

            item.count=mContext.getSelectedItemCountById(item.id);
            tvcount.setText(String.valueOf(item.count));
            price.setText(nf.format(item.price));

            if (item.count<1) {
                tvcount.setVisibility(View.GONE);
                tvminus.setVisibility(View.GONE);
            }else {
                tvcount.setVisibility(View.VISIBLE);
                tvminus.setVisibility(View.VISIBLE);
            }

        }
        @Override
        public void onClick(View v) {
            ShoppingCartActivity activity=mContext;
            switch (v.getId()){
                case R.id.tvAdd:{
                    int count=activity.getSelectedItemCountById(item.id);
                    if (count<1){
                        tvminus.setAnimation(getShowAnimation());
                        tvminus.setVisibility(View.VISIBLE);
                        tvcount.setVisibility(View.VISIBLE);
                    }

                    activity.add(item, false);
                    count++;
                    tvcount.setText(String.valueOf(count));
                    int[] loc = new int[2];
                    v.getLocationInWindow(loc);
                    activity.playAnimation(loc);
                }
                break;
                case R.id.tvMinus :{
                    int count = activity.getSelectedItemCountById(item.id);
                    if (count < 2) {
                        tvminus.setAnimation(getHiddenAnimation());
                        tvminus.setVisibility(View.GONE);
                        tvminus.setVisibility(View.GONE);
                    }
                    count--;
                    activity.remove(item, false);//activity.getSelectedItemCountById(item.id)
                    tvcount.setText(String.valueOf(count));

                }
                break;
                default:
                    break;
            }

        }
    }
    private Animation getShowAnimation(){
        AnimationSet set=new AnimationSet(true);
        RotateAnimation rotate=new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate=new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,2f,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha=new AlphaAnimation(0,1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }
    private Animation getHiddenAnimation(){
        AnimationSet set=new AnimationSet(true);
        RotateAnimation rotate=new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate=new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,2f,
                TranslateAnimation.RELATIVE_TO_SELF,0,
                TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha=new AlphaAnimation(1,0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;

    }
}
