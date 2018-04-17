package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.example.administrator.adapter.GoodsItemAdapter;
import com.example.administrator.bean.Goods;
import com.example.administrator.http.ReceiveByServlet;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;
import com.example.administrator.utils.ThreadPoolManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class GoodsList extends Activity {

    private ListView LvGoodsList;
    private GoodsItemAdapter myAdapter;
    private List<Goods> dataList;


    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_goods_list);

        init();
        initData();

    }

    private void init(){
        LvGoodsList = (ListView)findViewById(R.id.lv_goods_list);

    }

    private void initData(){
        dataList = new ArrayList<Goods>();
        ThreadPoolManager.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                dataList = new ReceiveByServlet().getGoodsList();
                L.i_crz("getGoodsList size is:"+dataList.size());
                Message msg = new Message();
                msg.obj = dataList;
                handler.sendMessage(msg);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(dataList == null){
                L.i_crz("GoodsLsit is null");
                return ;
            }
            myAdapter = new GoodsItemAdapter(dataList, getApplicationContext(), new GoodsItemAdapter.ChangeTextView() {
                @Override
                public void changeTheText(int position) {
                    L.i_crz("点击ChangeTextView,第"+position+"个");
                    for(Goods goods:dataList){
                        goods.setStatus(false);
                    }
                    dataList.get(position).setStatus(true);

                    Goods goods = dataList.get(position);
                    Intent intent = new Intent(GoodsList.this,GoodsDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("GoodsObj",(Serializable) goods);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    myAdapter.notifyDataSetChanged();
                }
            });
            LvGoodsList.setAdapter(myAdapter);
        }
    };
}
