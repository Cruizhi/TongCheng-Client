package com.example.administrator.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.administrator.tongcheng.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/24.
 */

public class ConversationActivity extends FragmentActivity {

    @BindView(R.id.name)
    TextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        ButterKnife.bind(this);
//        mName = (TextView) findViewById(R.id.name);

        String sId = getIntent().getData().getQueryParameter("targetId");//targetId:单聊即对方ID，群聊即群组ID
        String sName = getIntent().getData().getQueryParameter("title");//获取昵称

        if (!TextUtils.isEmpty(sName)){
            mName.setText(sName);
        }else {
//            sId
            //TODO 拿到id 去请求自己服务端
        }
    }
}
