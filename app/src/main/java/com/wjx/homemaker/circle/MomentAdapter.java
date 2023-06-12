package com.wjx.homemaker.circle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;
import com.wjx.homemaker.activity.ShowFriendInforActivity;
import com.wjx.homemaker.entity.CommunityEntity;
import com.wjx.homemaker.utils.AsyncImageLoader;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.FileCache;
import com.wjx.homemaker.utils.MemoryCache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.wjx.homemaker.activity.MyApplication.mAdapter;

/**
 * Created by huangziwei on 16-4-12.
 */
public class MomentAdapter extends BaseAdapter {

    public static final int VIEW_HEADER = 0;
    public static final int VIEW_MOMENT = 1;


    private int mstart, mend;

    private ArrayList<Moment> mList;
    public List<CommunityEntity.XX> list;
    private Context mContext;

    private Activity activity;
    private AsyncImageLoader imageLoader;//异步组件

    private ListView mlistView;

    private String[] text = new String[]{
        "浏览6次","浏览8次","浏览18次","浏览12次","浏览9次"
    };
//    public static String[] URLS;
//    public static String[] URLS_item1;
//    private boolean isfirst;
//    private ImageLoader imageLoader;
//    private ImageLoader2 imageLoader2;

    private CustomTagHandler mTagHandler;

    public MomentAdapter(Context context, Activity activity, ArrayList<Moment> mlist, ListView mlistView, List<CommunityEntity.XX> list, CustomTagHandler tagHandler) {
        this.mList = mlist;
        this.mContext = context;
        this.list = list;
        this.mlistView = mlistView;
        this.activity = activity;
//        this.mTagHandler = tagHandler;
//
//        imageLoader = new ImageLoader(mlistView);
////        imageLoader2 = new ImageLoader2(mlistView);
//
//        URLS = new String[list.size()];
//        URLS_item1 = new String[list.size()];
//
//        for(int i = 0 ; i< list.size() ; i++){
//            URLS[i] = "http://ali.nook.one" + list.get(i).userData.getAvatar();
////            if (list.get(i).attachments.size() != 0) {
////
////                URLS_item1[i] = "http://ali.nook.one" + list.get(i).attachments.get(0).getPath();
////            } else {
////                URLS_item1[i] = "";
////            }
//            URLS_item1[i] = "http://ali.nook.one" + list.get(i).userData.getAvatar();
//            Log.i("wjx", ""+i+" : " + URLS_item1[i]);
//        }
//        isfirst = true;
//        mlistView.setOnScrollListener(this);


        MemoryCache mcache = new MemoryCache();//内存缓存
        File sdCard = android.os.Environment.getExternalStorageDirectory();//获得SD卡
        File cacheDir = new File(sdCard, "jereh_cache");//缓存根目录
        FileCache fcache = new FileCache(context, cacheDir, "news_img");//文件缓存
        imageLoader = new AsyncImageLoader(context, mcache, fcache);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_MOMENT;
    }

    @Override
    public int getCount() {
        // heanderView
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.item_moment, null);
            ViewHolder holder = new ViewHolder();
            holder.mCommentList = (LinearLayout) convertView.findViewById(R.id.comment_list);
            holder.mBtnInput = convertView.findViewById(R.id.btn_input_comment2);
            holder.mContent = (TextView) convertView.findViewById(R.id.content);
            holder.mName = (TextView) convertView.findViewById(R.id.name);
            holder.show_img1 = (ImageView) convertView.findViewById(R.id.show_img1);
            holder.show_img2 = (ImageView) convertView.findViewById(R.id.show_img2);
            holder.show_img3 = (ImageView) convertView.findViewById(R.id.show_img3);
            holder.show_img4 = (ImageView) convertView.findViewById(R.id.show_img4);
            holder.show_img5 = (ImageView) convertView.findViewById(R.id.show_img5);
            holder.show_img6 = (ImageView) convertView.findViewById(R.id.show_img6);
            holder.show_img7 = (ImageView) convertView.findViewById(R.id.show_img7);
            holder.show_img8 = (ImageView) convertView.findViewById(R.id.show_img8);
            holder.show_img9 = (ImageView) convertView.findViewById(R.id.show_img9);
            holder.img_good = (ImageView) convertView.findViewById(R.id.moment_img_good);
            holder.img_moment = (ImageView) convertView.findViewById(R.id.moment_img_comment);
            holder.img_return = (ImageView) convertView.findViewById(R.id.moment_img_return);
            holder.tv_look = (TextView) convertView.findViewById(R.id.comment_tv_look);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.author_icon = (ImageView) convertView.findViewById(R.id.author_icon);
            convertView.setTag(holder);
        }
        //防止ListView的OnItemClick与item里面子view的点击发生冲突
        ((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        final int index = position;
        final ViewHolder holder = (ViewHolder) convertView.getTag();

//        String url = "http://ali.nook.one" + list.get(index).userData.getAvatar();

//        if (list.get(position).attachments.size() != 0) {
//
//            String url_item1 = "http://ali.nook.one" + list.get(index).userData.getAvatar();
//            holder.show_img1.setTag(url_item1);
//            holder.show_img1.setImageResource(R.mipmap.ic_launcher);
//            imageLoader2.showImageByAsyncTask(holder.show_img1, url_item1);
//        } else {
//            holder.show_img1.setVisibility(View.GONE);
//        }

//        String url_item1 = "http://ali.nook.one" + list.get(position).attachments.get(0).getPath();
//
//        if (!url_item1.equals("")) {
//            holder.show_img1.setTag(url_item1);
//            holder.show_img1.setImageResource(R.mipmap.ic_launcher);
//            imageLoader2.showImageByAsyncTask(holder.show_img1, url_item1);
//        } else {
//            holder.show_img1.setVisibility(View.GONE);
//        }

        holder.mContent.setText(list.get(index).getContent());
        holder.mName.setText(list.get(index).userData.getUsername());
        holder.time.setText(list.get(index).getCreateDate());

//        holder.author_icon.setTag(url);
//        holder.author_icon.setImageResource(R.mipmap.ic_launcher);
//        imageLoader.showImageByAsyncTask(holder.author_icon, url);

        holder.author_icon.setTag(Config.BASEHOST + list.get(index).userData.getAvatar());
        Bitmap bmp = imageLoader.loadBitmap(holder.author_icon, Config.BASEHOST + list.get(index).userData.getAvatar());
        if (bmp == null) {
            holder.author_icon.setImageResource(R.mipmap.ic_launcher);
        } else {
            holder.author_icon.setImageBitmap(bmp);
        }

        if (list.get(index).attachments.size() != 0) {


            for (int i = 0; i < list.get(index).attachments.size(); i++) {

                if (i == 0) {

                    holder.show_img1.setTag(Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    Bitmap bmp2 = imageLoader.loadBitmap(holder.show_img1, Config.BASEHOST  + list.get(index).attachments.get(i).getPath());
                    if (bmp2 == null) {
                        holder.show_img1.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        holder.show_img1.setImageBitmap(bmp2);
                    }
                    holder.show_img1.setVisibility(View.VISIBLE);

                }
                if (i == 1) {
                    holder.show_img2.setTag(Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    Bitmap bmp3 = imageLoader.loadBitmap(holder.show_img2, Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    if (bmp3 == null) {
                        holder.show_img2.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        holder.show_img2.setImageBitmap(bmp3);
                    }
                    holder.show_img2.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    holder.show_img3.setTag(Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    Bitmap bmp4 = imageLoader.loadBitmap(holder.show_img3, Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    if (bmp4 == null) {
                        holder.show_img3.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        holder.show_img3.setImageBitmap(bmp4);
                    }
                    holder.show_img3.setVisibility(View.VISIBLE);
                }
                if (i == 3) {
                    holder.show_img4.setTag(Config.BASEHOST  + list.get(index).attachments.get(i).getPath());
                    Bitmap bmp5 = imageLoader.loadBitmap(holder.show_img4, Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    if (bmp5 == null) {
                        holder.show_img4.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        holder.show_img4.setImageBitmap(bmp5);
                    }
                    holder.show_img4.setVisibility(View.VISIBLE);
                }
                if (i == 4) {
                    holder.show_img5.setTag(Config.BASEHOST  + list.get(index).attachments.get(i).getPath());
                    Bitmap bmp6 = imageLoader.loadBitmap(holder.show_img5, Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    if (bmp6 == null) {
                        holder.show_img5.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        holder.show_img5.setImageBitmap(bmp6);
                    }
                    holder.show_img5.setVisibility(View.VISIBLE);
                }

                if (i == 5) {
                    holder.show_img6.setTag(Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    Bitmap bmp7 = imageLoader.loadBitmap(holder.show_img6, Config.BASEHOST  + list.get(index).attachments.get(i).getPath());
                    if (bmp7 == null) {
                        holder.show_img6.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        holder.show_img6.setImageBitmap(bmp7);
                    }
                    holder.show_img6.setVisibility(View.VISIBLE);
                }
                if (i == 6) {
                    holder.show_img7.setTag(Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    Bitmap bmp8 = imageLoader.loadBitmap(holder.show_img3, Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    if (bmp8 == null) {
                        holder.show_img7.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        holder.show_img7.setImageBitmap(bmp8);
                    }
                    holder.show_img7.setVisibility(View.VISIBLE);
                }

                if (i == 7) {
                    holder.show_img8.setTag(Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    Bitmap bmp9 = imageLoader.loadBitmap(holder.show_img8, Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    if (bmp9 == null) {
                        holder.show_img8.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        holder.show_img8.setImageBitmap(bmp9);
                    }
                    holder.show_img8.setVisibility(View.VISIBLE);
                }
                if (i == 8) {
                    holder.show_img9.setTag(Config.BASEHOST  + list.get(index).attachments.get(i).getPath());
                    Bitmap bmp10 = imageLoader.loadBitmap(holder.show_img9, Config.BASEHOST + list.get(index).attachments.get(i).getPath());
                    if (bmp10 == null) {
                        holder.show_img9.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        holder.show_img9.setImageBitmap(bmp10);
                    }
                    holder.show_img9.setVisibility(View.VISIBLE);
                }
            }

        } else {
            holder.show_img1.setVisibility(View.GONE);
            holder.show_img2.setVisibility(View.GONE);
            holder.show_img3.setVisibility(View.GONE);
            holder.show_img4.setVisibility(View.GONE);
            holder.show_img5.setVisibility(View.GONE);
            holder.show_img6.setVisibility(View.GONE);
            holder.show_img7.setVisibility(View.GONE);
            holder.show_img8.setVisibility(View.GONE);
            holder.show_img9.setVisibility(View.GONE);
        }

        holder.mBtnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentFun.inputComment(activity, mlistView, view, MyApplication.muser, new CommentFun.InputCommentListener() {
                    @Override
                    public void onCommitComment() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        holder.img_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.img_good.setImageResource(R.mipmap.circle_good_press);
            }
        });

        holder.img_moment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mBtnInput.setClickable(true);

            }
        });

        holder.author_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ShowFriendInforActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("position", position);
                intent.putExtras(mBundle);
                mContext.startActivity(intent);
            }
        });

        holder.tv_look.setText(text[position]);

        CommentFun.parseCommentList(mContext, mList.get(index).mComment,
                holder.mCommentList, holder.mBtnInput, mTagHandler);
        return convertView;
    }

//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        // TODO Auto-generated method stub
//        if(scrollState == SCROLL_STATE_IDLE){
//            //加载可见项
//            imageLoader.loadImages(mstart, mend);
////            imageLoader2.loadImages(mstart, mend);
////            imageLoader3.loadImages(mstart, mend);
//        }else{
//            //停止任务
//            imageLoader.cancelAllTask();
////            imageLoader2.cancelAllTask();
////            imageLoader3.cancelAllTask();
//        }
//    }
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        // TODO Auto-generated method stub
//        mstart = firstVisibleItem;
//        mend = firstVisibleItem + visibleItemCount;
//
//        if(isfirst && visibleItemCount > 0){
//            imageLoader.loadImages(mstart, mend);
////            imageLoader2.loadImages(mstart, mend);
////            imageLoader3.loadImages(mstart, mend);
//            isfirst = false;
//        }
//    }

    /**
     * 删除按钮的监听接口
     */
    public interface onItemAddListener {
        void inputComment(View v);
    }

    private onItemAddListener mOnItemAddListener;

    public void setOnItemAddClickListener(onItemAddListener mOnItemAddListener) {
        this.mOnItemAddListener = mOnItemAddListener;
    }


    private static class ViewHolder {
        LinearLayout mCommentList;
        View mBtnInput;
        TextView mContent;
        TextView mName;
        TextView time;
        ImageView author_icon;
        ImageView show_img1;
        ImageView show_img2;
        ImageView show_img3;
        ImageView show_img4;
        ImageView show_img5;
        ImageView show_img6;
        ImageView show_img7;
        ImageView show_img8;
        ImageView show_img9;

        TextView tv_look;
        ImageView img_good;
        ImageView img_moment;
        ImageView img_return;

    }
}
