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

public class FulltimeDetail extends Activity implements View.OnClickListener{

    private Button BtBack;
    private TextView TvTitle;
    private TextView TvWages;
    private TextView TvDate;
    private TextView TvRequire;
    private TextView TvAddress;
    private TextView TvWelfare;
    private TextView TvContent;

    private LinearLayout LlPhone,LlChat;


    private String userid;

    private Recruit recruit;

    @Override
    public void onCreate(Bundle saveIntanceState){
        super.onCreate(saveIntanceState);
        setContentView(R.layout.activity_fulltimedetail);

        initRecruit();
        init();
    }

    private void initRecruit(){
        Bundle bundle = this.getIntent().getExtras();
        recruit = (Recruit) bundle.getSerializable("RecruitObj");
    }

    private void init(){
        TvTitle = (TextView)findViewById(R.id.tv_fulltimedetail_title);
        TvWages = (TextView)findViewById(R.id.tv_fulltimedetail_wages);
        TvDate = (TextView)findViewById(R.id.tv_fulltimedetail_date);
        TvRequire = (TextView)findViewById(R.id.tv_fulltimedetail_require);
        TvAddress = (TextView)findViewById(R.id.tv_fulltimedetail_address);
        TvWelfare = (TextView)findViewById(R.id.tv_fulltimedetail_welfare);
        TvContent = (TextView)findViewById(R.id.tv_fulltimedetail_content);

        LlChat = (LinearLayout) findViewById(R.id.ll_recruitdetail_chat);
        LlPhone = (LinearLayout) findViewById(R.id.ll_recruitdetail_phone);
        LlChat.setOnClickListener(this);
        LlPhone.setOnClickListener(this);

        TvTitle.setText(recruit.getName());
        TvWages.setText(recruit.getWages());
        TvDate.setText(recruit.getDate());
        TvRequire.setText("");
        TvAddress.setText(recruit.getAddress());
        TvWelfare.setText(recruit.getWelfare());
        TvContent.setText(recruit.getContent());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_recruitdetail_chat:
                if(RongIM.getInstance() != null){
                    RongIM.getInstance().startPrivateChat(this,recruit.getUserid(),recruit.getUserid());
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
