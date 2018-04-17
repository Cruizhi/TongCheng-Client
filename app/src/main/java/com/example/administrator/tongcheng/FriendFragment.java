package com.example.administrator.tongcheng;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2017/10/24.
 */

public class FriendFragment extends Fragment {
    public static FriendFragment instance = null;//单例模式

    public static FriendFragment getInstance() {
        if (instance == null) {
            instance = new FriendFragment();
        }

        return instance;
    }

    private View mView;
    private Button mButton_Friend;
    private Button mButton_Customer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.friend_fragment,null);
        mButton_Friend = (Button) mView.findViewById(R.id.friend);
        mButton_Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(getActivity(), "10086", "聊天");
                }
            }
        });

        mButton_Customer = (Button) mView.findViewById(R.id.customer);
        mButton_Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RongIM.getInstance()!=null){
                    RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.APP_PUBLIC_SERVICE, "KEFU145793828389012", "在线客服--杨羽嫣");
                }
            }
        });


//        TextView tv = new TextView(getActivity());
//        tv.setText("第三页");
//        return tv;
        return mView;

    }
}
