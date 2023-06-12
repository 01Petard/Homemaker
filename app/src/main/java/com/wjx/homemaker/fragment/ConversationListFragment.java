package com.wjx.homemaker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.AddFriendActivity;
import com.wjx.homemaker.activity.ContactListActivity;
import com.wjx.homemaker.activity.MyChatActivity;

import java.util.List;

/**
 * Created by admin on 2017/7/17.
 */

public class ConversationListFragment extends Fragment implements View.OnClickListener{

    private View rootView;


    private EaseConversationListFragment easeConversationListFragment;
    
    private LinearLayout ll_add;
    private LinearLayout ll_addresslist;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_conversation, null);

        initView();

        return rootView;
    }

    private void initView() {

        easeConversationListFragment=new EaseConversationListFragment();
        easeConversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent=new Intent(getActivity(), MyChatActivity.class);
                //传入参数
                Bundle args=new Bundle();
                args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                args.putString(EaseConstant.EXTRA_USER_ID,conversation.conversationId());
                intent.putExtra("conversation",args);
                startActivity(intent);
            }
        });

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.my_conversation_list,easeConversationListFragment)
                .commit();

        ll_add = (LinearLayout) rootView.findViewById(R.id.con_ll_add);
        ll_addresslist = (LinearLayout) rootView.findViewById(R.id.con_ll_addresslist);

        ll_add.setOnClickListener(this);
        ll_addresslist.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.con_ll_add:
//                Toast.makeText(getActivity(), "111", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), AddFriendActivity.class);

                startActivityForResult(intent, 0);

                break;

            case R.id.con_ll_addresslist:
                Intent intent2 = new Intent(getActivity(), ContactListActivity.class);

                startActivityForResult(intent2, 0);

                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        switch (requestCode) {
            case 0:
                Log.i("wjx", "111: ");
                break;
        }
    }

    private void refreshUIWithMessage() {
        new Thread(new Runnable() {
            public void run() {
                // refresh unread count
                // refresh conversation list
                if (easeConversationListFragment != null) {
                    easeConversationListFragment.refresh();
                }
            }
        });
    }

    EMMessageListener messageListener=new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //接收到新的消息
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };


    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }
}
