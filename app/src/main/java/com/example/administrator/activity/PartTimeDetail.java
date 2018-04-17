package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.bean.Recruit;
import com.example.administrator.tongcheng.R;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/4/15.
 */

public class PartTimeDetail extends Activity implements View.OnClickListener {
    private Button BtBack;
    private TextView TvTitle;
    private TextView TvWages;
    private TextView TvDate;
    private TextView TvAddress;
    private TextView TvContent;
    private TextView TvDuration;
    private TextView TvPeriod;
    private TextView TvCompany;
    private TextView TvPayroll;

    private LinearLayout LlPhone,LlChat;


    private String userid;

    private Recruit recruit;

    @Override
    public void onCreate(Bundle saveIntanceState){
        super.onCreate(saveIntanceState);
        setContentView(R.layout.activity_parttimedetail);

        initRecruit();
        init();
    }

    private void initRecruit(){
        Bundle bundle = this.getIntent().getExtras();
        recruit = (Recruit) bundle.getSerializable("RecruitObj");
    }

    private void init(){
        TvTitle = (TextView)findViewById(R.id.tv_parttimedetail_title);
        TvWages = (TextView)findViewById(R.id.tv_parttimedetail_wages);
        TvDate = (TextView)findViewById(R.id.tv_parttimedetail_date);
        TvAddress = (TextView)findViewById(R.id.tv_parttimedetail_address);
        TvContent = (TextView)findViewById(R.id.tv_parttimedetail_content);
        TvCompany = (TextView)findViewById(R.id.tv_parttimedetail_company);
        TvDuration = (TextView)findViewById(R.id.tv_parttimedetail_duration);
        TvPeriod = (TextView)findViewById(R.id.tv_parttimedetail_period);
        TvPayroll = (TextView)findViewById(R.id.tv_parttimedetail_payroll);

        LlChat = (LinearLayout) findViewById(R.id.ll_recruitdetail_chat);
        LlPhone = (LinearLayout) findViewById(R.id.ll_recruitdetail_phone);
        LlChat.setOnClickListener(this);
        LlPhone.setOnClickListener(this);

        TvTitle.setText(recruit.getName());
        TvWages.setText(recruit.getWages());
        TvDate.setText(recruit.getDate());
        TvAddress.setText(recruit.getAddress());
        TvContent.setText(recruit.getContent());
        TvCompany.setText(recruit.getCompany());
        TvDuration.setText(recruit.getDuration());
        TvPeriod.setText(recruit.getPeriod());
        TvPayroll.setText(recruit.getPayroll());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_recruitdetail_chat:
                if(RongIM.getInstance() != null){
                    RongIM.getInstance().startPrivateChat(this,recruit.getUserid(),recruit.getLinkman());
                }
                break;
            case R.id.ll_recruitdetail_phone:
                Intent intent=new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:"+recruit.getLinkphone()));
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
