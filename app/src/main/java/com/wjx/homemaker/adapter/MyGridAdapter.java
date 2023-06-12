package com.wjx.homemaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjx.homemaker.R;
import com.wjx.homemaker.helper.BaseViewHolder;

/**
 * Created by admin on 2017/7/14.
 */

public class MyGridAdapter extends BaseAdapter {

    private Context mContext;

    private String[] img_text ;
    private int[] imgs ;

    public MyGridAdapter(Context mContext,String[] img_text,int[] imgs) {
        super();
        this.mContext = mContext;
        this.img_text = img_text;
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return img_text.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.my_grid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
        ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
        iv.setBackgroundResource(imgs[position]);
        tv.setText(img_text[position]);
        return convertView;
    }
}
