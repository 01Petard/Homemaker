package com.wjx.homemaker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjx.homemaker.R;
import com.wjx.homemaker.activity.MyApplication;
import com.wjx.homemaker.activity.PublishedActivity;
import com.wjx.homemaker.circle.Comment;
import com.wjx.homemaker.circle.CommentFun;
import com.wjx.homemaker.circle.CustomTagHandler;
import com.wjx.homemaker.circle.Moment;
import com.wjx.homemaker.circle.MomentAdapter;
import com.wjx.homemaker.circle.User;
import com.wjx.homemaker.entity.CommunityEntity;
import com.wjx.homemaker.utils.Config;
import com.wjx.homemaker.utils.GsonUtil;

import java.util.ArrayList;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by admin on 2017/7/17.
 */

public class CircleFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    private LinearLayout con_ll_addnew;

    public static User sUsers = new User(2, "wjx"); // 当前登录用户
    private ListView mListView;
    private MomentAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_circle, null);

        initView();

        initData();

        return rootView;
    }

    private void initData() {

        new Thread(new Runnable() {
            @Override
            public void run() {


                HttpUtils httpUtils = new HttpUtils();
                httpUtils.send(HttpRequest.HttpMethod.GET, Config.GETNEWS, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String json = responseInfo.result;
                        CommunityEntity communityEntity = GsonUtil.fromJson(json, CommunityEntity.class);


                        if (communityEntity.errcode == 0) {

                            MyApplication.friend_list = communityEntity.list;

                            ArrayList<Moment> moments = new ArrayList<>();
                            for (int i = 0; i < communityEntity.list.size(); i++) {
                                ArrayList<Comment> comments = null;
                                if (i < 1) {
                                    comments = new ArrayList<Comment>();
                                    comments.add(new Comment(new User(i + 300, "叶敏杰"), "是哈，这个天气热的都不想动", null));
                                } else if(i<2) {
                                    comments = new ArrayList<Comment>();
                                    comments.add(new Comment(new User(i + 300, "叶敏杰"), "我要，我要，可以帮我买下么？", null));
                                    comments.add(new Comment(new User(i + 300, "冯亮"), "看着个学习的好高级啊", null));
                                } else if (i < 3) {
                                    comments = new ArrayList<Comment>();
                                    comments.add(new Comment(new User(i + 300, "叶敏杰"), "运气不错嘛", null));
                                } else if (i < 4) {
                                    comments = new ArrayList<Comment>();
                                    comments.add(new Comment(new User(i + 300, "叶敏杰"), "求爆照", null));
                                } else if (i< 5){
                                    comments = new ArrayList<Comment>();
                                    comments.add(new Comment(new User(i + 300, "冯亮"), "还可以吧", null));
                                }else {
                                    comments = new ArrayList<Comment>();
                                    comments.add(new Comment(new User(i + 300, "冯亮"), "厉害了啊", null));
                                }
                                moments.add(new Moment("动态 " + i, comments));

                            }

                            mAdapter = new MomentAdapter(getActivity(),getActivity(), moments,mListView, communityEntity.list, new CustomTagHandler(getActivity(), new CustomTagHandler.OnCommentClickListener() {
                                @Override
                                public void onCommentatorClick(View view, User commentator) {
                                    Toast.makeText(getApplicationContext(), commentator.mName, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onReceiverClick(View view, User receiver) {
                                    Toast.makeText(getApplicationContext(), receiver.mName, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onContentClick(View view, final User commentator, final User receiver) {
                                    if (commentator != null && commentator.mId == sUsers.mId) { // 不能回复自己的评论
                                        return;
                                    }

//                                    user_con = commentator;
//                                    inputComment(view, commentator);
                                    MyApplication.muser = commentator;
                                }
                            }));

                            MyApplication.mAdapter = mAdapter;
                            mListView.setAdapter(mAdapter);
//
//                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    inputComment(view, user_con);
//                                }
//                            });
                        }
                    }

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        Toast.makeText(getApplicationContext(),
                                exception.getMessage() + " = " + msg,
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        }).start();
    }

    private void initView() {

        con_ll_addnew = (LinearLayout) rootView.findViewById(R.id.con_ll_addnew);
        mListView = (ListView) rootView.findViewById(R.id.list_moment2);

        con_ll_addnew.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.con_ll_addnew:


                Intent intent = new Intent(getActivity(), PublishedActivity.class);

                startActivity(intent);
                break;
        }
    }

    public void inputComment(final View v) {
        inputComment(v, null);
    }

    public void inputComment(final View v, User receiver) {
        CommentFun.inputComment(getActivity(), mListView, v, receiver, new CommentFun.InputCommentListener() {
            @Override
            public void onCommitComment() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
