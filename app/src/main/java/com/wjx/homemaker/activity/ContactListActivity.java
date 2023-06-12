package com.wjx.homemaker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.wjx.homemaker.R;
import com.wjx.homemaker.entity.FriendEntity;
import com.wjx.homemaker.utils.APPConfig;
import com.wjx.homemaker.utils.AsyncImageLoader;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.FileCache;
import com.wjx.homemaker.utils.MemoryCache;
import com.wjx.homemaker.utils.SharedPreferencesUtils;

import java.io.File;
import java.util.List;

import static com.wjx.homemaker.activity.MyApplication.userEntity;

/**
 * Created by admin on 2017/7/17.
 */

public class ContactListActivity extends AppCompatActivity {
    private ListView contact_list;

    private LinearLayout ll_qun;

    private AsyncImageLoader imageLoader;//异步组件

    private String userName = MyApplication.userEntity.userData.getUsername();
    private String avatar = Config.BASEHOST + userEntity.userData.getAvatar();

    private static EaseContactListFragment contactListFragment;

    private LinearLayout information_linear_back;

    List<String> name = MyApplication.userNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactlist);
        initView();

        initData();
    }

    private void initData() {

        MyAdapter adapter = new MyAdapter(getApplicationContext(), MyApplication.friendEntity.list);
        contact_list.setAdapter(adapter);
        contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), ShowFriendInfor2Activity.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("position", i);
                intent.putExtras(mBundle);
                startActivity(intent);
//                startChat(MyApplication.friendEntity.list.get(i).getUsername());
            }
        });


    }

    private void initView() {

        ll_qun = (LinearLayout) findViewById(R.id.ll_qun);

        contact_list = (ListView) findViewById(R.id.contact_list2);
        information_linear_back = (LinearLayout) findViewById(R.id.information_linear_back);

        information_linear_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_qun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), QunActivity.class);
                startActivity(intent);

            }
        });

    }

    private void startChat(String name) {

        //设置要发送出去的昵称
        SharedPreferencesUtils.setParam(this, APPConfig.USER_NAME, userName);
        //设置要发送出去的头像
        SharedPreferencesUtils.setParam(this, APPConfig.USER_HEAD_IMG, avatar);

        Intent intent = new Intent(ContactListActivity.this, MyChatActivity.class);

        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, name);
        intent.putExtra("conversation", args);

        startActivity(intent);
    }

    private class MyAdapter extends BaseAdapter {


        public List<FriendEntity.Friend> list;

        public MyAdapter(Context context, List<FriendEntity.Friend> list) {
            this.list = list;
            MemoryCache mcache = new MemoryCache();//内存缓存
            File sdCard = android.os.Environment.getExternalStorageDirectory();//获得SD卡
            File cacheDir = new File(sdCard, "jereh_cache2");//缓存根目录
            FileCache fcache = new FileCache(context, cacheDir, "news_img2");//文件缓存
            imageLoader = new AsyncImageLoader(context, mcache, fcache);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            LayoutInflater inflater = getLayoutInflater();
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.contactlist_item, null);
                holder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
                holder.tv_name = (TextView) convertView.findViewById(R.id.con_tv_name);
                holder.tv_score = (TextView) convertView.findViewById(R.id.con_tv_score);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.tv_name.setText(list.get(position).getUsername());
            holder.tv_score.setText("亲密度:" + list.get(position).getScore());

            holder.item_img.setTag(Config.BASEHOST+ list.get(position).getAvatar());
            Bitmap bmp = imageLoader.loadBitmap(holder.item_img,Config.BASEHOST + list.get(position).getAvatar());
            if (bmp == null) {
                holder.item_img.setImageResource(R.mipmap.ic_launcher);
            } else {
                holder.item_img.setImageBitmap(bmp);
            }
            return convertView;
        }

    }


    private class ViewHolder {
        public ImageView item_img;
        public TextView tv_name;
        public TextView tv_score;
    }

}
