package com.example.administrator.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.activity.Login;
import com.example.administrator.ui.activity.Setting;
import com.example.administrator.ui.activity.UserDetail;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mob.tools.utils.DeviceHelper.getApplication;

/**
 * Created by Administrator on 2018/2/2.
 */

public class User_F extends Fragment implements View.OnClickListener{

    @BindView(R.id.rl_user_top)
    RelativeLayout RlHead;
    @BindView(R.id.btn_user_setting)
    Button BtnSetting;
    @BindView(R.id.iv_user_indent)
    ImageView IvIndent;
    @BindView(R.id.iv_user_delivery)
    ImageView IvDeliery;
    @BindView(R.id.iv_user_evaluate)
    ImageView IvEvaluate;
    @BindView(R.id.iv_user_release)
    ImageView IvRelease;
    @BindView(R.id.iv_user_collection)
    ImageView IvCollection;
    @BindView(R.id.tv_user_username)
    TextView TvUsername;
    @BindView(R.id.tv_user_userphone)
    TextView TvUserphone;

    //当前用户信息
    private String userid;
    private String username;
    private String password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        //引入布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user,null);
        //设置全屏
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);

        ButterKnife.bind(this,view);

        getuserinfo();  //获取当前用户信息
        init(view);  //初始化设置
        L.i_crz("User_F--onCreateView");

        return view;
    }

    private void getuserinfo(){
        SharedPreferences shareP = getActivity().getSharedPreferences("userinfo",getApplication().MODE_PRIVATE);
        userid = shareP.getString("phone","");
        username = shareP.getString("username","");

    }

    private void init(View view){

        if(userid != null && !"".equals(userid)){
            TvUsername.setText("用户名:" + username);
            TvUserphone.setText("账号:" + userid);
        }else{
            TvUsername.setText("用户名:");
            TvUserphone.setText("账号:");
        }
    }

//    @Override
    @OnClick({R.id.iv_user_collection,R.id.iv_user_release,R.id.iv_user_evaluate,R.id.iv_user_delivery,
    R.id.iv_user_indent,R.id.rl_user_top,R.id.btn_user_setting})
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rl_user_top:
                SharedPreferences shareP = getActivity().getSharedPreferences("userinfo",getApplication().MODE_PRIVATE);
                String userid = shareP.getString("phone","");
                if(!"".equals(userid)){
                    //用户设置
                    Intent intent0 = new Intent(getApplication(), UserDetail.class);
                    startActivity(intent0);

                }else{
                    Intent intent1 = new Intent(getApplication(),Login.class);
                    startActivity(intent1);
                }
                break;
            case R.id.btn_user_setting:
                Intent intent1 = new Intent(getApplication(),Setting.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences shareP = getActivity().getSharedPreferences("userinfo",getApplication().MODE_PRIVATE);
        userid = shareP.getString("phone","");
        username = shareP.getString("username","");

        TvUsername.setText("用户名:" + username);
        TvUserphone.setText("账号:" + userid);

        L.i_crz("User_F--onResume--userid:"+userid);
    }
    
}
