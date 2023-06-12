package tudan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;

import java.util.List;

import tudan.Bean.User;

/**
 * Created by fengliang
 * on 2017/7/21.
 */

public class FriendsAdapter extends BaseAdapter{
    private List<User> mList;
    private Context mContext;
    private TextView username,uid;
    public FriendsAdapter(Context context,List<User> list){
        this.mContext=context;
        this.mList=list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        convertView=layoutInflater.inflate(R.layout.adapter_friends,null);
        username= (TextView) convertView.findViewById(R.id.username);
        uid= (TextView) convertView.findViewById(R.id.userid);
        if (mList.get(position).getUid()!=null){
        username.setText(mList.get(position).getUsername());
        uid.setText("(uid:"+mList.get(position).getUid()+")");}
        else {
            username.setText("");
            uid.setText("");
        }
        MyApplication.setRobuid(uid.getText().toString());


        return convertView;
    }
}
