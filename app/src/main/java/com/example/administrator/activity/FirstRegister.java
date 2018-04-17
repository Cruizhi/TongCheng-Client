package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.tongcheng.R;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2018/2/7.
 */

public class FirstRegister extends Activity implements View.OnClickListener {

    private EditText Etphone;  //电话号码
    private EditText EtverificationCode;  //验证码
    private Button BtverificationCode;  //获取验证码
    private Button Btnext;  //下一步
    private Button Btback;  //返回

    private String phone;
    private String verificationCode;

    private boolean flag;  //判断操作是否成功

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        MobSDK.init(this);  //初始化mob
        init();  //初始化控件

        EventHandler eventHandler = new EventHandler(){  //操作回调(开启线程)
            @Override
            public void afterEvent(int event,int result,Object data){
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);  //用来往SMSSDK中注册一个事件接收器(注册回调接口)
    }

    private void init(){
        Etphone = (EditText)findViewById(R.id.et_register1_phone);
        EtverificationCode = (EditText)findViewById(R.id.et_register1_verification);
        BtverificationCode = (Button)findViewById(R.id.btn_register1_verification);
        Btnext = (Button)findViewById(R.id.btn_register1_next);
        Btback = (Button)findViewById(R.id.btn_register1_back);
        BtverificationCode.setOnClickListener(this);
        Btnext.setOnClickListener(this);
        Btback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register1_verification:
                if(!TextUtils.isEmpty(Etphone.getText())){
                    if(Etphone.getText().length() == 11){
                        phone = Etphone.getText().toString();
                        SMSSDK.getVerificationCode("86",phone);  //发送验证码；86为区号
                        EtverificationCode.requestFocus();
                   }else{
                        Toast.makeText(this,"请输入正确的电话号码",Toast.LENGTH_SHORT).show();
                        Etphone.requestFocus();
                    }
                }else{
                    Toast.makeText(this,"请输入电话号码",Toast.LENGTH_SHORT).show();
                    Etphone.requestFocus();
                }
                break;
            case R.id.btn_register1_next:
                if(!TextUtils.isEmpty(EtverificationCode.getText())){
                    if(EtverificationCode.getText().length() == 4){
                        verificationCode = EtverificationCode.getText().toString();
                        SMSSDK.submitVerificationCode("86",phone,verificationCode);
                        flag = false;
                    }else{
                        Toast.makeText(this,"请输入正确的验证码",Toast.LENGTH_SHORT).show();
                        EtverificationCode.requestFocus();
                    }
                }else{
                    Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
                    EtverificationCode.requestFocus();
                }
                break;
            case R.id.btn_register1_back:
                this.finish();
                break;
            default:
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            if(result == SMSSDK.RESULT_COMPLETE){
                //操作成功
                if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    //校验验证码，返回手机号和国家代码
                    Toast.makeText(FirstRegister.this,"验证成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FirstRegister.this,SecondRegister.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Toast.makeText(FirstRegister.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                }else if(event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
                }
            }else{
                //操作失败
                if(flag){
                    Toast.makeText(FirstRegister.this,"验证码获取失败",Toast.LENGTH_SHORT).show();
                    Etphone.requestFocus();
                }else{
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(FirstRegister.this,"验证码错误",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();  //注销回调接口
    }
}
