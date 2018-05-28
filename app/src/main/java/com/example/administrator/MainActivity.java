package com.example.administrator;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.tongcheng.R;
import com.example.administrator.ui.fragment.Cart_F;
import com.example.administrator.ui.fragment.Home_F;
import com.example.administrator.ui.fragment.Release_F;
import com.example.administrator.ui.fragment.User_F;
import com.example.administrator.utils.L;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    //界面底部菜单按钮
    private ImageView[] bt_menu = new ImageView[5];

    //界面底部菜单按钮id
    private int[] bt_menu_id = {R.id.iv_menu_0,R.id.iv_menu_1,R.id.iv_menu_2,R.id.iv_menu_3,R.id.iv_menu_4};

    //界面底部选中菜单按钮的资源
    private int[] select_on = {R.mipmap.ic_menu_0_selected,R.mipmap.ic_menu_1_selected,R.mipmap.ic_menu_2_selected,
    R.mipmap.ic_menu_3_selected,R.mipmap.ic_menu_4_selected};

    //界面底部未选中按钮的资源
    private int[] select_off = {R.mipmap.ic_menu_0_normal,R.mipmap.ic_menu_1_normal,R.mipmap.ic_menu_2_normal,
    R.mipmap.ic_menu_3_normal,R.mipmap.ic_menu_4_normal};

    //Fragment界面
    private Home_F home_f;
    private User_F user_f;
    private Fragment message_f;
    private Release_F release_f;
    private Cart_F cart_f;

    //当前用户信息
    private String userphone;
    private String usertoken;
    private String useraddress;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();  //初始化控件
        setSelect(0);  //绑定默认首页
        getUserinfo();  //获取当前用户信息
        connectRongServer(usertoken);

    }

    private void initView(){
        for(int i = 0; i<bt_menu.length; i++){
            bt_menu[i] = (ImageView)findViewById(bt_menu_id[i]);
            bt_menu[i].setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v){
        resetImage();
        switch (v.getId()){
            case R.id.iv_menu_0:
                setSelect(0);
                break;
            case R.id.iv_menu_1:
                setSelect(1);
                break;
            case R.id.iv_menu_2:
                setSelect(2);
                break;
            case R.id.iv_menu_3:
                setSelect(3);
                break;
            case R.id.iv_menu_4:
                setSelect(4);
                break;
            default:
                break;
        }
    }

    //切换显示内容，将点击图标设置为亮
    private void setSelect(int i){
        FragmentManager fm = getSupportFragmentManager();
        //创建一个新的事务
        FragmentTransaction transaction = fm.beginTransaction();
        //隐藏所有Fragment,然后处理要显示的Fragment
        hideFragment(transaction);
        switch(i){
            case 0:
                if(home_f == null){  //若是无定义fragment，则显示空白
                    home_f = new Home_F();
                    transaction.add(R.id.content_layout,home_f);
                }else{
                    transaction.show(home_f);
                }
                bt_menu[0].setImageResource(select_on[0]);
                break;
            case 1:
                if(message_f == null){
//                    message_f = new Message_F();
                    message_f = initConversationList();  //获取会话列表
                    transaction.add(R.id.content_layout,message_f);
                }else{
                    transaction.show(message_f);
                }
                bt_menu[1].setImageResource(select_on[1]);
                break;
            case 2:
                if(release_f == null){
                    release_f = new Release_F();
                    transaction.add(R.id.content_layout,release_f);
                }else{
                    transaction.show(release_f);
                }
                bt_menu[2].setImageResource(select_on[2]);
                break;
            case 3:
                if(cart_f == null){
                    cart_f = new Cart_F();
                    transaction.add(R.id.content_layout,cart_f);
                }else{
                    transaction.show(cart_f);
                }
                bt_menu[3].setImageResource(select_on[3]);
                break;
            case 4:
                if(user_f == null){
                    user_f = new User_F();
                    transaction.add(R.id.content_layout,user_f);
                }else{
                    transaction.show(user_f);
                }
                bt_menu[4].setImageResource(select_on[4]);
                break;
            default:
                break;
        }
        transaction.commit();//提交事务
    }

    //隐藏所有Fragment
    private void hideFragment(FragmentTransaction transaction){
        if(home_f != null){
            transaction.hide(home_f);
        }
        if(message_f != null){
            transaction.hide(message_f);
        }
        if(release_f != null){
            transaction.hide(release_f);
        }
        if(cart_f != null){
            transaction.hide(cart_f);
        }
        if(user_f != null){
            transaction.hide(user_f);
        }
    }

    //显示底部按钮未点击时的图标
    private void resetImage(){
        for(int i = 0; i < bt_menu.length; i++){
            bt_menu[i].setImageResource(select_off[i]);
        }
    }

    //获取会话列表
    private Fragment initConversationList(){
        if(message_f == null){
            ConversationListFragment listFragment = new ConversationListFragment();
            Uri uri=Uri.parse("rong://"+getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(),"false")
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                    .build();
            listFragment.setUri(uri);
            return listFragment;
        }else{
            return message_f;
        }
    }

    private void getUserinfo(){
        SharedPreferences shareP = getSharedPreferences("userinfo",getApplication().MODE_PRIVATE);
        userphone = shareP.getString("phone","");
        useraddress = shareP.getString("address","");
        usertoken = shareP.getString("token","");
    }

    //登录融云，用户上线
    private void connectRongServer(String token){
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            @Override
            public void onSuccess(String s) {
                L.i_crz("RongServer success,userid:"+s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

}
