package com.wjx.homemaker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.wjx.homemaker.R;
import com.wjx.homemaker.fragment.MyChatFragment;

/**
 * Created by admin on 2017/7/10.
 */

public class MyChatActivity extends FragmentActivity {

    private EaseChatFragment chatFragment;
    private MyChatFragment myChatFragment;
    private String TAG="MyChatActivity";
    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow =  false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mychat);

        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("conversation");
//        bundle.getString(EaseConstant.EXTRA_USER_ID);
//        chatFragment=new EaseChatFragment();
//        chatFragment.setArguments(bundle);
        myChatFragment=new MyChatFragment();
        myChatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.conversation_container,myChatFragment)
                .commit();

    }
}
