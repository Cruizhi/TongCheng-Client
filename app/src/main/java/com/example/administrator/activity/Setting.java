package com.example.administrator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.tongcheng.R;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/3/4.
 */

public class Setting extends Activity implements View.OnClickListener{

    private Button BtnQuitBack;  //退出登录
    private Button BtnBack;  //返回

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    private void init(){
        BtnQuitBack = (Button)findViewById(R.id.btn_setting_quitback);
        BtnBack = (Button)findViewById(R.id.btn_setting_back);
        BtnQuitBack.setOnClickListener(this);
        BtnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_setting_back:
                this.finish();
                break;
            case R.id.btn_setting_quitback:
                new AlertDialog.Builder(this).setTitle("提示框").setMessage("是否确定退出")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedP = getSharedPreferences("userinfo", getApplication().MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedP.edit();
                                editor.putString("userid","");  //添加
                                editor.putString("phone","");
                                editor.putString("password","");
                                editor.putString("email","");
                                editor.putString("username","");
                                editor.putString("sex","");
                                editor.putString("address","");
                                editor.putString("pic","");
                                editor.putString("token","");
                                editor.commit();//提交
                                RongIM.getInstance().disconnect();  //退出融云服务
                            }
                        }).setNegativeButton("取消",null).show();

                break;
            default:
                break;
        }
    }
}
