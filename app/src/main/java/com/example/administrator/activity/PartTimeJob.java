package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.http.UploadByServlet;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.RegionUtils;
import com.example.administrator.utils.ThreadPoolManager;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Administrator on 2018/3/22.
 */

public class PartTimeJob extends Activity implements View.OnClickListener{

    private String url = "PartTimeJob";

    private EditText EtName;  //兼职名称
    private EditText EtType;  //兼职类型
    private EditText EtNum;  //兼职人数
    private RadioButton RbLongtime,RbShorttime;  //兼职时效
    private RadioGroup RgPeriod;  //时间段
    private RadioButton RbChoose,RbArbitrariness;  //兼职时段
    private LinearLayout LlPeriod;  //是否显示选择时段
    private EditText EtPeriod;  //选择时段
    private EditText EtWages;  //资薪
    private Spinner SpPayroll;  //结薪方式
    private LinearLayout LlAddress;  //地址选择按钮
    private TextView TvAddress;  //详细地址
    private EditText EtContent;  //工作内容
    private EditText EtLinkman;  //联系内容
    private EditText EtLinkphone;  //联系电话
    private EditText Etcompany;  //公司名称
    private Button BtCancel;  //取消按钮
    private Button BtSubmit;  //提交按钮

    private String Type = "parttime";
    private String Duration;  //时效
    private String Period;  //时段
    private String userid;
    private String usertoken;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_parttime_release);

        getuserinfo();
        init();
        initClick();
    }

    private void getuserinfo(){
        SharedPreferences shareP = getSharedPreferences("userinfo",getApplication().MODE_PRIVATE);
        userid = shareP.getString("phone","");
        usertoken = shareP.getString("token","");
    }

    private void init(){
        EtName = (EditText)findViewById(R.id.et_parttime_name);
        EtType = (EditText)findViewById(R.id.et_parttime_type);
        EtNum = (EditText)findViewById(R.id.et_parttime_num);
        RbLongtime = (RadioButton)findViewById(R.id.rb_parttime_long);
        RbShorttime = (RadioButton)findViewById(R.id.rb_parttime_short);
        RgPeriod = (RadioGroup)findViewById(R.id.rg_parttime_period);
        RbChoose = (RadioButton)findViewById(R.id.rb_parttime_choose);
        RbArbitrariness = (RadioButton)findViewById(R.id.rb_parttime_arbitrariness);
        LlPeriod = (LinearLayout)findViewById(R.id.ll_parttime_periode);
        EtPeriod = (EditText)findViewById(R.id.et_parttime_periode);
        EtWages = (EditText)findViewById(R.id.et_parttime_wages);
        SpPayroll = (Spinner)findViewById(R.id.sp_parttime_payroll);
        LlAddress = (LinearLayout)findViewById(R.id.ll_parttime_address);
        TvAddress = (TextView)findViewById(R.id.tv_parttime_address);
        EtContent = (EditText)findViewById(R.id.et_parttime_content);
        EtLinkman = (EditText)findViewById(R.id.et_parttime_linkman);
        EtLinkphone = (EditText)findViewById(R.id.et_parttime_linkphone);
        Etcompany = (EditText)findViewById(R.id.et_parttime_company);
        BtCancel = (Button)findViewById(R.id.btn_parttime_cancel);
        BtSubmit = (Button)findViewById(R.id.btn_parttime_submit);
        LlAddress.setOnClickListener(this);
        BtCancel.setOnClickListener(this);
        BtSubmit.setOnClickListener(this);
    }

    private void initClick(){
        RgPeriod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.rb_parttime_choose){
                    LlPeriod.setVisibility(View.VISIBLE);
                    Period = EtPeriod.getText().toString();
                }else{
                    LlPeriod.setVisibility(View.GONE);
                    Period = "任意";
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_parttime_address:
                Intent intent0 = new Intent(PartTimeJob.this, RegionUtils.class);
                startActivityForResult(intent0,102);
                break;
            case R.id.btn_parttime_cancel:
                finish();
                break;
            case R.id.btn_parttime_submit:
                final String payroll = SpPayroll.getSelectedItem().toString();
                if(RbLongtime.isChecked()){
                    Duration = "长期";
                }else{
                    Duration = "短期";
                }
                if(Period.equals("")){
                    Period = "任意";
                }else {
                    Period = EtPeriod.getText().toString();
                }
                ThreadPoolManager.getInstance().addTask(new Runnable() {
                    @Override
                    public void run() {
                        BasicNameValuePair bnvp0 = new BasicNameValuePair("recruittype",Type);
                        BasicNameValuePair bnvp1 = new BasicNameValuePair("type",EtType.getText().toString());
                        BasicNameValuePair bnvp2 = new BasicNameValuePair("name",EtName.getText().toString());
                        BasicNameValuePair bnvp3 = new BasicNameValuePair("num",EtNum.getText().toString());
                        BasicNameValuePair bnvp4 = new BasicNameValuePair("duration",Duration);
                        BasicNameValuePair bnvp5 = new BasicNameValuePair("period",Period);
                        BasicNameValuePair bnvp6 = new BasicNameValuePair("wages",EtWages.getText().toString());
                        BasicNameValuePair bnvp7 = new BasicNameValuePair("payroll",payroll);
                        BasicNameValuePair bnvp8 = new BasicNameValuePair("address",TvAddress.getText().toString());
                        BasicNameValuePair bnvp9 = new BasicNameValuePair("content",EtContent.getText().toString());
                        BasicNameValuePair bnvp10 = new BasicNameValuePair("linkman",EtLinkman.getText().toString());
                        BasicNameValuePair bnvp11 = new BasicNameValuePair("linkphone",EtLinkphone.getText().toString());
                        BasicNameValuePair bnvp12 = new BasicNameValuePair("company",Etcompany.getText().toString());
                        BasicNameValuePair bnvp13 = new BasicNameValuePair("userid",userid);
                        BasicNameValuePair bnvp14 = new BasicNameValuePair("usertoken",usertoken);
                        String response = UploadByServlet.post(url,bnvp0,bnvp1,bnvp2,bnvp3,bnvp4,bnvp5,bnvp6,
                                bnvp7,bnvp8,bnvp9,bnvp10,bnvp11,bnvp12,bnvp13,bnvp14);
                        Message msg = new Message();
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }
                });
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        switch (requestCode){
            case 102:
                if(requestCode == 102){
                    TvAddress.setText(intent.getStringExtra("address"));
                }
                break;
            default:
                break;
        }
    }


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            String response = (String)msg.obj;
            if(response.equals("true")){
                Toast.makeText(PartTimeJob.this,"发布成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(PartTimeJob.this,"发布失败，请重新发布",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
