package com.example.administrator.tongcheng;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2018/2/24.
 */

public class Main extends Activity implements View.OnClickListener, RongIM.UserInfoProvider{

    private static final String token1 = "frBCI/57SiDA1ShLoBm87niU1lvLB1OLIyZpQB68dIBPyboDEuv89lle/jS71/RtebM62N3od7H5seQVKf0W3w==";
    private static final String token2 = "gJaOOoSTpX2G7eb12uT/59uxdkt/KHl+zftarYq8PylHA/ZCWEG19zwDmEGWwldihc662C7xJg0yF2ZQRyBWrQ==";

    private List<Friend> userIdList;
    private static final String TAG = "Main";

    private Button mUser1, mUser2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mUser1 = (Button) findViewById(R.id.connect_10010);
        mUser2 = (Button) findViewById(R.id.connect_10086);
        mUser1.setOnClickListener(this);
        mUser2.setOnClickListener(this);


        initUserInfo();

    }

    private void connectRongServer(String token) {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {


            @Override
            public void onSuccess(String userId) {

                if (userId.equals("10010")) {
                    mUser1.setText("用户1连接服务器成功");
                    startActivity(new Intent(Main.this, HomeActivity.class));
                    Toast.makeText(Main.this, "connect server success 10010", Toast.LENGTH_SHORT).show();

                } else {

                    startActivity(new Intent(Main.this, HomeActivity.class));
                    Toast.makeText(Main.this, "connect server success 10086", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                // Log.e("onError", "onError userid:" + errorCode.getValue());//获取错误的错误码
                Log.e(TAG, "connect failure errorCode is : " + errorCode.getValue());
            }


            @Override
            public void onTokenIncorrect() {
                Log.e(TAG, "token is error ,please check token and appkey");
            }
        });

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.connect_10010) {
            connectRongServer(token1);
        } else if (v.getId() == R.id.connect_10086) {
            connectRongServer(token2);
        }
    }


    private void initUserInfo() {
        userIdList = new ArrayList<Friend>();
        userIdList.add(new Friend("10010", "联通", "http://www.51zxw.net/bbs/UploadFile/2013-4/201341122335711220.jpg"));//联通图标
        userIdList.add(new Friend("10086", "移动", "http://img02.tooopen.com/Download/2010/5/22/20100522103223994012.jpg"));//移动图标
        userIdList.add(new Friend("KEFU144542424649464","在线客服","http://img02.tooopen.com/Download/2010/5/22/20100522103223994012.jpg"));
        RongIM.setUserInfoProvider(this, true);
    }

    @Override
    public UserInfo getUserInfo(String s) {

        for (Friend i : userIdList) {
            if (i.getUserId().equals(s)) {
                Log.e(TAG, i.getPortraitUri());
                return new UserInfo(i.getUserId(), i.getName(), Uri.parse(i.getPortraitUri()));
            }
        }


        Log.e("MainActivity", "UserId is : " + s);
        return null;
    }
}
