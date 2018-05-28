package com.example.administrator.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bean.CheckBool;
import com.example.administrator.tongcheng.R;
import com.example.administrator.ui.fragment.Welfare_F;
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

public class FullTimeJob extends AppCompatActivity implements View.OnClickListener{

    private String url ="FullTimeJob";

    @BindView(R.id.et_fulltime_name)
    EditText EtName;  //招聘岗位
    @BindView(R.id.ll_fulltime_wages)
    LinearLayout LlWages;
    @BindView(R.id.et_fulltime_wages1)
    EditText EtWages0;  //月薪
    @BindView(R.id.et_fulltime_wages2)
    EditText EtWages1;
    @BindView(R.id.tv_fulltime_negotiation)
    TextView TvNegotiation;  //面议
    @BindView(R.id.rb_fulltim_negotiation)
    RadioButton RbNegotiation;  //面议按钮
    @BindView(R.id.et_fulltime_content)
    EditText EtContent;  //工作内容
    @BindView(R.id.sp_fulltime_qualifications)
    Spinner SpQualifications;  //学历要求
    @BindView(R.id.ll_fulltime_welfare)
    LinearLayout LlWelfare;  //福利待遇
    @BindView(R.id.tv_fulltime_welfare)
    TextView TvWelfare;  //福利待遇
    @BindView(R.id.ll_fulltime_address)
    LinearLayout LlAddress;   //详细地址
    @BindView(R.id.tv_fulltime_address)
    TextView TvAddress;  //详细地址
    @BindView(R.id.et_fulltime_linkphone)
    EditText EtLinkphone;  //联系电话
    @BindView(R.id.btn_fulltime_cancel)
    Button BtCancel;  //取消按钮
    @BindView(R.id.btn_fulltime_submit)
    Button BtSubmit;  //发布按钮

    private String wages;
    private String Type = "fulltime";
    private String userid;
    private String usertoken;

    private CheckBool checkBool = new CheckBool();

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_fulltime_release);

        ButterKnife.bind(this);
        getuserinfo();

    }

    private void getuserinfo(){
        SharedPreferences shareP = getSharedPreferences("userinfo",getApplication().MODE_PRIVATE);
        userid = shareP.getString("phone","");
        usertoken = shareP.getString("token","");
    }

//    @Override
    @OnClick({R.id.rb_fulltim_negotiation,R.id.ll_fulltime_welfare,R.id.ll_fulltime_address,
            R.id.btn_fulltime_cancel,R.id.btn_fulltime_submit})
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rb_fulltim_negotiation:
                boolean isCheck = checkBool.getCheck();
                if(isCheck){
                    LlWages.setVisibility(View.VISIBLE);
                    TvNegotiation.setVisibility(View.GONE);
                    RbNegotiation.setChecked(false);
                }else{
                    LlWages.setVisibility(View.GONE);
                    TvNegotiation.setVisibility(View.VISIBLE);
                    RbNegotiation.setChecked(true);
                }
                checkBool.setCheck(!isCheck);
                L.i_crz("RbNegotiation -- "+isCheck);
                break;
            case R.id.ll_fulltime_welfare:
                Welfare_F welfare_f = new Welfare_F();
                welfare_f.show(getSupportFragmentManager(),"android");
                welfare_f.setOnDialogListener(new Welfare_F.OnDialogListener() {
                    @Override
                    public void onDialogClick(String person) {
                        TvWelfare.setText(person);
                    }
                });
                break;
            case R.id.ll_fulltime_address:
                Intent intent0 = new Intent(FullTimeJob.this, RegionUtils.class);
                startActivityForResult(intent0,102);
                break;
            case R.id.btn_fulltime_cancel:
                finish();
                break;
            case R.id.btn_fulltime_submit:
                final String qualifications = SpQualifications.getSelectedItem().toString();
                wages = EtWages0.getText().toString();
                if(wages.equals("")){
                    wages = "面议";
                }else{
                    wages = EtWages0.getText().toString()+"--"+EtWages1.getText().toString();
                }

                Map<String, String> params = new HashMap<>();
                params.put("name", EtName.getText().toString());
                params.put("wages",wages);
                params.put("welfare",TvWelfare.getText().toString());
                params.put("content",EtContent.getText().toString());
                params.put("qualifications",qualifications);
                params.put("linkphone",EtLinkphone.getText().toString());
                params.put("address",TvAddress.getText().toString());
                params.put("userid",userid);
                params.put("usertoken",usertoken);
                params.put("recruit",Type);
                OkHttpUtils.post()//
                        .url(getUrl()+url)//
                        .params(params)//
                        .build()//
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                L.i_crz("FullTime--submit:"+e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if(response.equals("true")){
                                    Toast.makeText(FullTimeJob.this,"发布成功",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(FullTimeJob.this, "发布失败，请重新发布", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

//                ThreadPoolManager.getInstance().addTask(new Runnable() {
//                    @Override
//                    public void run() {
//                        BasicNameValuePair bnvp0 = new BasicNameValuePair("name",EtName.getText().toString());
//                        BasicNameValuePair bnvp1 = new BasicNameValuePair("wages",wages);
//                        BasicNameValuePair bnvp2 = new BasicNameValuePair("welfare",TvWelfare.getText().toString());
//                        BasicNameValuePair bnvp3 = new BasicNameValuePair("content",EtContent.getText().toString());
//                        BasicNameValuePair bnvp4 = new BasicNameValuePair("qualifications",qualifications);
//                        BasicNameValuePair bnvp5 = new BasicNameValuePair("linkphone",EtLinkphone.getText().toString());
//                        BasicNameValuePair bnvp6 = new BasicNameValuePair("address",TvAddress.getText().toString());
//                        BasicNameValuePair bnvp7 = new BasicNameValuePair("userid",userid);
//                        BasicNameValuePair bnvp8 = new BasicNameValuePair("usertoken",usertoken);
//                        BasicNameValuePair bnvp9 = new BasicNameValuePair("recruit",Type);
//                        String response = UploadByServlet.post(url,bnvp0,bnvp1,bnvp2,bnvp3,bnvp4,bnvp5,bnvp6,bnvp7,bnvp8,bnvp9);
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

//    private Handler handler = new Handler(){
//        public void handleMessage(Message msg){
//            String response = (String)msg.obj;
//            if(response.equals("true")){
//                Toast.makeText(FullTimeJob.this,"发布成功",Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(FullTimeJob.this, "发布失败，请重新发布", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };

}
