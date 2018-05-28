package com.example.administrator.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.activity.Login;
import com.example.administrator.http.UploadByServlet;
import com.example.administrator.tongcheng.R;

import org.apache.http.message.BasicNameValuePair;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/8.
 */

public class SecondRegister extends Activity{

    @BindView(R.id.et_register2_username)
    EditText Etusername;  //用户姓名
    @BindView(R.id.et_register2_password)
    EditText Etpassword;  //密码
    @BindView(R.id.et_register2_password_confirm)
    EditText Etpasswd_confirm;  //确认密码
    @BindView(R.id.et_register2_address)
    EditText Etaddress;   //所在地
    @BindView(R.id.et_register2_email)
    EditText Etemail;  //邮箱
    @BindView(R.id.btn_register2_back)
    Button Btback;  //返回
    @BindView(R.id.btn_register2_register)
    Button Btregister;  //注册按钮Servlet

    private String phone;  //电话号码
    private String url = "RegisterServlet";   //服务器地址

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        ButterKnife.bind(this);

    }

//    @Override
    @OnClick({R.id.btn_register2_register,R.id.btn_register2_back})
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_register2_back:
                this.finish();
                break;
            case R.id.btn_register2_register:
                String passwd = Etpassword.getText().toString().trim();
                String passwd_confirm = Etpasswd_confirm.getText().toString().trim();
                if(!passwd.equals(passwd_confirm)){
                    Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                }else if("".equals(passwd) || "".equals(passwd_confirm)){
                    Toast.makeText(this,"密码不能为空，请输入",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent  = getIntent();
                    if(intent != null){
                        phone = intent.getStringExtra("phone");
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BasicNameValuePair bnvp0 = new BasicNameValuePair("username",Etusername.getText().toString());
                            BasicNameValuePair bnvp1 = new BasicNameValuePair("phone",phone);
                            BasicNameValuePair bnvp2 = new BasicNameValuePair("email",Etemail.getText().toString());
                            BasicNameValuePair bnvp3 = new BasicNameValuePair("address",Etaddress.getText().toString());
                            BasicNameValuePair bnvp4 = new BasicNameValuePair("password",Etpassword.getText().toString());
                            String response = UploadByServlet.post(url,bnvp0,bnvp1,bnvp2,bnvp3,bnvp4);
                            Message msg = new Message();
                            msg.obj = response;
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String response = (String)msg.obj;
            if(response.equals("true")){
                Toast.makeText(SecondRegister.this,"注册成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SecondRegister.this,Login.class);
                startActivity(intent);
                SecondRegister.this.finish();
            }else{
                Toast.makeText(SecondRegister.this,"注册失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

}