package com.example.administrator.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.adapter.GoodsDetailAdapter;
import com.example.administrator.bean.Goods;
import com.example.administrator.tongcheng.GoodsPj_F;
import com.example.administrator.tongcheng.GoodsSp_F;
import com.example.administrator.tongcheng.GoodsXq_F;
import com.example.administrator.tongcheng.R;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/4/9.
 */

public class GoodsDetail extends FragmentActivity implements View.OnClickListener{

    private ViewPager VpGoodsDetail;

    private LinearLayout LlGoodsSp,LlGoodsXq,LlGoodsPj;

    private TextView TvGoodsSp,TvGoodsXq,TvGoodsPj;

    private GoodsDetailAdapter myAdapter;

    private LinearLayout LlConversion;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    private Goods thegoods;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_goods_detail);

        initGoods();
        init();
    }

    private void initGoods(){
        Bundle bundle = this.getIntent().getExtras();
        thegoods = (Goods) bundle.getSerializable("GoodsObj");
    }

    private void init(){
        VpGoodsDetail = (ViewPager)findViewById(R.id.vp_goods_content);
        LlGoodsSp = (LinearLayout)findViewById(R.id.ll_goods_sp);
        LlGoodsXq = (LinearLayout)findViewById(R.id.ll_goods_xq);
        LlGoodsPj = (LinearLayout)findViewById(R.id.ll_goods_pj);
        TvGoodsSp = (TextView)findViewById(R.id.tv_goods_sp);
        TvGoodsXq = (TextView)findViewById(R.id.tv_goods_xq);
        TvGoodsPj = (TextView)findViewById(R.id.tv_goods_pj);
        LlGoodsSp.setOnClickListener(this);
        LlGoodsXq.setOnClickListener(this);
        LlGoodsPj.setOnClickListener(this);

        LlConversion = (LinearLayout)findViewById(R.id.ll_goods_conversion);
        LlConversion.setOnClickListener(this);

        GoodsSp_F goodsSp_f = new GoodsSp_F();
        GoodsXq_F goodsXq_f = new GoodsXq_F();
        GoodsPj_F goodsPj_f = new GoodsPj_F();
        fragmentList.add(goodsSp_f);
        fragmentList.add(goodsXq_f);
        fragmentList.add(goodsPj_f);

        myAdapter = new GoodsDetailAdapter(getSupportFragmentManager(),fragmentList);
        VpGoodsDetail.setAdapter(myAdapter);
        VpGoodsDetail.setOnPageChangeListener(new VpOnChangeListener());
    }

    @Override
    public void onClick(View v) {
        ResetTabsColor();
        switch (v.getId()){
            case R.id.ll_goods_sp:
                setTabsSelectColor(0);
                break;
            case R.id.ll_goods_xq:
                setTabsSelectColor(1);
                break;
            case R.id.ll_goods_pj:
                setTabsSelectColor(2);
                break;
            case R.id.ll_goods_conversion:
                if(RongIM.getInstance() != null){
                    RongIM.getInstance().startPrivateChat(this,thegoods.getUserid(),thegoods.getUserid());
                }
                break;
            default:
                break;
        }
    }

    private void setTabsSelectColor(int i){
        switch(i){
            case 0:
                TvGoodsSp.setTextColor(getResources().getColor(R.color.top_Red));
                break;
            case 1:
                TvGoodsXq.setTextColor(getResources().getColor(R.color.top_Red));
                break;
            case 2:
                TvGoodsPj.setTextColor(getResources().getColor(R.color.top_Red));
                break;
            default:
                break;
        }
        VpGoodsDetail.setCurrentItem(i);
    }

    private void ResetTabsColor(){
        TvGoodsSp.setTextColor(getResources().getColor(R.color.bg_Gray));
        TvGoodsXq.setTextColor(getResources().getColor(R.color.bg_Gray));
        TvGoodsPj.setTextColor(getResources().getColor(R.color.bg_Gray));
    }

    class VpOnChangeListener extends ViewPager.SimpleOnPageChangeListener{
        @Override
        public void onPageSelected(int position){
            ResetTabsColor();
            setTabsSelectColor(position);
        }
    }

}
