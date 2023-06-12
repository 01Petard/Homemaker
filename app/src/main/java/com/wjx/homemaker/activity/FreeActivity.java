package com.wjx.homemaker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wjx.homemaker.R;
import com.wjx.homemaker.entity.FriendEntity;

import tudan.Activity.ShoppingCartActivity;

/**
 * Created by admin on 2017/7/22.
 */

public class FreeActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout information_linear_back;
    private ListView list_free;
    private FriendEntity friend = MyApplication.friendEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free);

        initView();
    }

    private void initView() {

        information_linear_back = (LinearLayout) findViewById(R.id.information_linear_back3);

        list_free = (ListView) findViewById(R.id.free_listView);

        MyAdapter adapter = new MyAdapter();

        list_free.setAdapter(adapter);

        information_linear_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.information_linear_back3:

                finish();

                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return friend.list.size();
        }

        @Override
        public Object getItem(int i) {
            return friend.list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater inflater = getLayoutInflater();
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.friend_listview_item, null);
                holder.friend_btn_start = (Button) convertView.findViewById(R.id.friend_btn_start);
                holder.friend_tv_fill = (TextView) convertView.findViewById(R.id.friend_tv_fill);
                holder.tv_line = (TextView) convertView.findViewById(R.id.tv_line);
                holder.friend_tv_text = (TextView) convertView.findViewById(R.id.friend_tv_text3);
                holder.friend_tv_number = (TextView) convertView.findViewById(R.id.friend_tv_number);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.friend_tv_text.setText("限只能同好友" + friend.list.get(position).getUsername() + "之间使用");
            holder.friend_btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(MyApplication.getInstance(), ShoppingCartActivity.class));


                }
            });

            return convertView;
        }
    }

    private class ViewHolder{
        public TextView friend_tv_number;
        public TextView friend_tv_fill;
        public Button friend_btn_start;
        public TextView friend_tv_text;
        public TextView tv_line;
    }

}
