package com.example.administrator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.bean.User;
import com.example.administrator.http.ReceiveByServlet;
import com.example.administrator.http.UploadByServlet;
import com.example.administrator.tongcheng.MainActivity;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2018/1/23.
 */

public class Login extends Activity implements OnClickListener,RongIM.UserInfoProvider{

    private EditText userid;  //账号
    private EditText password;  //密码
    private ImageView Ivshowpasswd;  //是否显示密码
    private Boolean showpasswd = false;  //默认不显示密码
    private Button Btndologin;  //登录按钮
    private Button Btnback;  //返回按钮
    private Button Btnforget;  //忘记密码按钮
    private Button Btnregister;  //注册按钮

    private List<User> luser = new ArrayList<>();

    private String url = "LoginServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init(); //初始化控件
    }

    public void init(){
        userid = (EditText)findViewById(R.id.et_login_id);
        password = (EditText)findViewById(R.id.et_login_password);
        Ivshowpasswd = (ImageView)findViewById(R.id.iv_login_showpasswd);
        Btnback = (Button)findViewById(R.id.btn_login_back);
        Btndologin = (Button)findViewById(R.id.btn_login_sure);
        Btnforget = (Button)findViewById(R.id.btn_login_forget);
        Btnregister = (Button)findViewById(R.id.btn_login_register);
        Ivshowpasswd.setImageDrawable(getResources().getDrawable(R.mipmap.ic_passwd_colse));  //默认不显示密码
        Btnback.setOnClickListener(this);
        Btndologin.setOnClickListener(this);
        Btnforget.setOnClickListener(this);
        Btnregister.setOnClickListener(this);
        Ivshowpasswd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_login_back:
                this.finish();
                break;
            case R.id.btn_login_forget:

                break;
            case R.id.btn_login_register:
                Intent intent = new Intent(this,FirstRegister.class);
                startActivity(intent);
                break;
            case R.id.btn_login_sure:
                String uid = userid.getText().toString();
                String passwd = password.getText().toString();
                if(!"".equals(uid) && !"".equals(passwd)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BasicNameValuePair bnvp0 = new BasicNameValuePair("userid",userid.getText().toString());
                            BasicNameValuePair bnvp1 = new BasicNameValuePair("password",password.getText().toString());
                            String response = UploadByServlet.post(url,bnvp0,bnvp1);
                            Message msg = new Message();
                            msg.obj = response;
                            handler.sendMessage(msg);
                        }
                    }).start();
                }else if("".equals(uid) || "".equals(passwd)){
                    new AlertDialog.Builder(this).setTitle("登录错误！").setMessage("账号或密码不能为空，" +
                            "\n请输入后登录!").create().show();
                }else{
                    Toast.makeText(this,"登录失败，系统错误！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_login_showpasswd:
                if(!showpasswd){  //显示密码
                    Ivshowpasswd.setImageDrawable(getResources().getDrawable(R.mipmap.ic_passwd_open));
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                    showpasswd = !showpasswd;
                }else{  //隐藏密码
                    Ivshowpasswd.setImageDrawable(getResources().getDrawable(R.mipmap.ic_passwd_colse));
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                    showpasswd = !showpasswd;
                }
                break;
            default:
                break;
        }
    }

    public Handler handler = new Handler(){
        public void handleMessage(Message msg){
            String response = (String)msg.obj;
            if(response.equals("false")){

                new AlertDialog.Builder(Login.this).setTitle("登录失败")
                        .setMessage("账号密码不正确。\n请检查后重新输入!").create().show();

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        BasicNameValuePair bnvp = new BasicNameValuePair("userid",userid.getText().toString());
//                        String response1 = UploadByServlet.post("UserServlet",bnvp);
//                    }
//                }).start();

            }else{
                //若用户存在则获取用户信息并且把信息保存到本地
                ReceiveByServlet receive = new ReceiveByServlet();
                User user = new User();
                user = receive.getUserinformation(response);
                SharedPreferences sharePreferences = getSharedPreferences("userinfo",getApplication().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharePreferences.edit();  //编辑SharePreferences
                editor.putString("userid",user.getUserid());  //添加
                editor.putString("phone",user.getPhone());
                editor.putString("password",user.getPassword());
                editor.putString("email",user.getEmail());
                editor.putString("username",user.getUsername());
                editor.putString("sex",user.getSex());
                editor.putString("address",user.getAddress());
                editor.putString("pic",user.getPic());
                editor.putString("token",user.getToken());
                editor.commit();//提交
                luser.add(user);
                RongIM.setUserInfoProvider(Login.this,true);
                connectRongServer(user.getToken());  //当前登录融云的的用户
                L.i_crz("Login--Handler--userid:"+user.getUserid());
                Toast.makeText(getApplication(),"登录成功!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
        }
    };

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

    @Override
    public UserInfo getUserInfo(String s) {
        for(User u : luser){
            if(u.getPhone().equals(s)){
                return new UserInfo(u.getPhone(),u.getUsername(), Uri.parse("http://123.207.246.18:8080/TC/"+u.getPic()));
            }
        }
        L.i_crz("Login-UserInfo-userid:"+s);
        return null;
    }
}
