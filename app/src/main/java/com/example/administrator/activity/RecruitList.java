package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.adapter.RecruitAdapter;
import com.example.administrator.bean.Recruit;
import com.example.administrator.http.ReceiveByServlet;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;
import com.example.administrator.utils.ThreadPoolManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class RecruitList extends Activity {

    private ListView LvRecruitList;

    private RecruitAdapter myAdapter;
    private List<Recruit> recruitList;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_recruitlist);

        init();
        getRecruitData();
    }

    private void getRecruitData(){
        recruitList = new ArrayList<Recruit>();
        ThreadPoolManager.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                recruitList = new ReceiveByServlet().getRecruitList();
                L.i_crz("getRecruitList size is:"+recruitList.size());
                Message msg = new Message();
                msg.obj = recruitList;
                handler.sendMessage(msg);
            }
        });

    }

    private void init(){
        LvRecruitList = (ListView)findViewById(R.id.lv_recruit_list);
        LvRecruitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recruit recruit = recruitList.get(position);
                String type = recruit.getRecruittype();
                L.i_crz("onitemclick--type:"+type);
                if(type.equals("parttime")){
                    Bundle bundle = new Bundle();
                    Intent intent0 = new Intent(RecruitList.this, PartTimeDetail.class);
                    bundle.putSerializable("RecruitObj",(Serializable) recruit);
                    intent0.putExtras(bundle);
                    startActivity(intent0);
                }else{
                    Bundle bundle = new Bundle();
                    Intent intent1 = new Intent(RecruitList.this,FulltimeDetail.class);
                    bundle.putSerializable("RecruitObj",(Serializable) recruit);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(recruitList == null){
                L.i_crz("RecruitList--recruitlist is null");
                return ;
            }
            myAdapter = new RecruitAdapter(RecruitList.this,recruitList);
            LvRecruitList.setAdapter(myAdapter);
        }
    };
}
