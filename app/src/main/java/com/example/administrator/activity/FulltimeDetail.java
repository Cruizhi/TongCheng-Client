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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/4/15.
 */

public class FulltimeDetail extends Activity{

    Button BtBack;
    @BindView(R.id.tv_fulltimedetail_title)
    TextView TvTitle;
    @BindView(R.id.tv_fulltimedetail_wages)
    TextView TvWages;
    @BindView(R.id.tv_fulltimedetail_date)
    TextView TvDate;
    @BindView(R.id.tv_fulltimedetail_require)
    TextView TvRequire;
    @BindView(R.id.tv_fulltimedetail_address)
    TextView TvAddress;
    @BindView(R.id.tv_fulltimedetail_welfare)
    TextView TvWelfare;
    @BindView(R.id.tv_fulltimedetail_content)
    TextView TvContent;

    @BindView(R.id.ll_recruitdetail_chat)
    LinearLayout LlChat;
    @BindView(R.id.ll_recruitdetail_phone)
    LinearLayout LlPhone;


    private String userid;

    private Recruit recruit;

    @Override
    public void onCreate(Bundle saveIntanceState){
        super.onCreate(saveIntanceState);
        setContentView(R.layout.activity_fulltimedetail);

        ButterKnife.bind(this);
        initRecruit();
        init();
    }

    private void initRecruit(){
        Bundle bundle = this.getIntent().getExtras();
        recruit = (Recruit) bundle.getSerializable("RecruitObj");
    }

    private void init(){
        TvTitle.setText(recruit.getName());
        TvWages.setText(recruit.getWages());
        TvDate.setText(recruit.getDate());
        TvRequire.setText("");
        TvAddress.setText(recruit.getAddress());
        TvWelfare.setText(recruit.getWelfare());
        TvContent.setText(recruit.getContent());

    }

//    @Override
    @OnClick({R.id.ll_recruitdetail_chat,R.id.ll_recruitdetail_phone})
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
