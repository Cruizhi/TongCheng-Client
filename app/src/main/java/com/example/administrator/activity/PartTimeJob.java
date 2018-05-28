package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;
import com.example.administrator.utils.RegionUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.example.administrator.http.UploadByServlet.getUrl;

/**
 * Created by Administrator on 2018/3/22.
 */

public class PartTimeJob extends Activity{

    private String url = "PartTimeJob";

    @BindView(R.id.et_parttime_name)
    EditText EtName;  //兼职名称
    @BindView(R.id.et_parttime_type)
    EditText EtType;  //兼职类型
    @BindView(R.id.et_parttime_num)
    EditText EtNum;  //兼职人数
    @BindView(R.id.rb_parttime_long)
    RadioButton RbLongtime;  //兼职时效
    @BindView(R.id.rb_parttime_short)
    RadioButton RbShorttime;
    @BindView(R.id.rg_parttime_period)
    RadioGroup RgPeriod;  //时间段
    @BindView(R.id.rb_parttime_choose)
    RadioButton RbChoose;  //兼职时段
    @BindView(R.id.rb_parttime_arbitrariness)
    RadioButton RbArbitrariness;
    @BindView(R.id.ll_parttime_periode)
    LinearLayout LlPeriod;  //是否显示选择时段
    @BindView(R.id.et_parttime_periode)
    EditText EtPeriod;  //选择时段
    @BindView(R.id.et_parttime_wages)
    EditText EtWages;  //资薪
    @BindView(R.id.sp_parttime_payroll)
    Spinner SpPayroll;  //结薪方式
    @BindView(R.id.ll_parttime_address)
    LinearLayout LlAddress;  //地址选择按钮
    @BindView(R.id.tv_parttime_address)
    TextView TvAddress;  //详细地址
    @BindView(R.id.et_business_content)
    EditText EtContent;  //工作内容
    @BindView(R.id.et_parttime_linkman)
    EditText EtLinkman;  //联系内容
    @BindView(R.id.et_parttime_linkphone)
    EditText EtLinkphone;  //联系电话
    @BindView(R.id.et_parttime_company)
    EditText Etcompany;  //公司名称
    @BindView(R.id.btn_parttime_cancel)
    Button BtCancel;  //取消按钮
    @BindView(R.id.btn_parttime_submit)
    Button BtSubmit;  //提交按钮

    private String Type = "parttime";
    private String Duration;  //时效
    private String Period;  //时段
    private String userid;
    private String usertoken;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_parttime_release);

        ButterKnife.bind(this);
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

//    @Override
    @OnClick({ R.id.ll_parttime_address,R.id.btn_parttime_cancel,R.id.btn_parttime_submit})
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

                Map<String, String> params = new HashMap<>();
                params.put("recruittype",Type);
                params.put("name", EtName.getText().toString());
                params.put("type",EtType.getText().toString());
                params.put("num",EtNum.getText().toString());
                params.put("duration",Duration);
                params.put("period",Period);
                params.put("wages",EtWages.getText().toString());
                params.put("payroll",payroll);
                params.put("content",EtContent.getText().toString());
                params.put("linkman",EtLinkman.getText().toString());
                params.put("linkphone",EtLinkphone.getText().toString());
                params.put("address",TvAddress.getText().toString());
                params.put("userid",userid);
                params.put("usertoken",usertoken);
                params.put("company",Etcompany.getText().toString());
                OkHttpUtils.post()//
                        .url(getUrl()+url)//
                        .params(params)//
                        .build()//
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                L.i_crz("PartTime--submit:"+e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if(response.equals("true")){
                                    Toast.makeText(PartTimeJob.this,"发布成功",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(PartTimeJob.this, "发布失败，请重新发布", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

//                ThreadPoolManager.getInstance().addTask(new Runnable() {
//                    @Override
//                    public void run() {
//                        BasicNameValuePair bnvp0 = new BasicNameValuePair("recruittype",Type);
//                        BasicNameValuePair bnvp1 = new BasicNameValuePair("type",EtType.getText().toString());
//                        BasicNameValuePair bnvp2 = new BasicNameValuePair("name",EtName.getText().toString());
//                        BasicNameValuePair bnvp3 = new BasicNameValuePair("num",EtNum.getText().toString());
//                        BasicNameValuePair bnvp4 = new BasicNameValuePair("duration",Duration);
//                        BasicNameValuePair bnvp5 = new BasicNameValuePair("period",Period);
//                        BasicNameValuePair bnvp6 = new BasicNameValuePair("wages",EtWages.getText().toString());
//                        BasicNameValuePair bnvp7 = new BasicNameValuePair("payroll",payroll);
//                        BasicNameValuePair bnvp8 = new BasicNameValuePair("address",TvAddress.getText().toString());
//                        BasicNameValuePair bnvp9 = new BasicNameValuePair("content",EtContent.getText().toString());
//                        BasicNameValuePair bnvp10 = new BasicNameValuePair("linkman",EtLinkman.getText().toString());
//                        BasicNameValuePair bnvp11 = new BasicNameValuePair("linkphone",EtLinkphone.getText().toString());
//                        BasicNameValuePair bnvp12 = new BasicNameValuePair("company",Etcompany.getText().toString());
//                        BasicNameValuePair bnvp13 = new BasicNameValuePair("userid",userid);
//                        BasicNameValuePair bnvp14 = new BasicNameValuePair("usertoken",usertoken);
//                        String response = UploadByServlet.post(url,bnvp0,bnvp1,bnvp2,bnvp3,bnvp4,bnvp5,bnvp6,
//                                bnvp7,bnvp8,bnvp9,bnvp10,bnvp11,bnvp12,bnvp13,bnvp14);
//                        Message msg = new Message();
//                        msg.obj = response;
//                        handler.sendMessage(msg);
//                    }
//                });
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

}
