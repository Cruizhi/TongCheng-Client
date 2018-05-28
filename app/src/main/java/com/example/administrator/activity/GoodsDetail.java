package com.example.administrator.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.adapter.GoodsDetailAdapter;
import com.example.administrator.bean.Goods;
import com.example.administrator.tongcheng.R;
import com.example.administrator.ui.fragment.GoodsPj_F;
import com.example.administrator.ui.fragment.GoodsSp_F;
import com.example.administrator.ui.fragment.GoodsXq_F;
import com.example.administrator.utils.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.MediaType;

import static com.example.administrator.http.UploadByServlet.getUrl;

/**
 * Created by Administrator on 2018/4/9.
 */

public class GoodsDetail extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.vp_goods_content)
    ViewPager VpGoodsDetail;
    @BindView(R.id.ll_goods_sp)
    LinearLayout LlGoodsSp;
    @BindView(R.id.ll_goods_xq)
    LinearLayout LlGoodsXq;
    @BindView(R.id.ll_goods_pj)
    LinearLayout LlGoodsPj;
    @BindView(R.id.tv_goods_xq)
    TextView TvGoodsXq;
    @BindView(R.id.tv_goods_sp)
    TextView TvGoodsSp;
    @BindView(R.id.tv_goods_pj)
    TextView TvGoodsPj;
    @BindView(R.id.ll_goods_conversion)
    LinearLayout LlConversion;
    @BindView(R.id.btn_goods_cart)
    Button BtCart;
    @BindView(R.id.btn_goods_buy)
    Button BtBuy;

    private String url = "AddCartServlet";

    private GoodsDetailAdapter myAdapter;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    private Goods thegoods;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_goods_detail);

        ButterKnife.bind(this);
        initGoods();
        init();
    }

    private void initGoods(){
        Bundle bundle = this.getIntent().getExtras();
        thegoods = (Goods) bundle.getSerializable("GoodsObj");
    }

    private void init(){

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

//    @Override
    @OnClick({R.id.ll_goods_xq,R.id.ll_goods_sp,R.id.ll_goods_pj,R.id.ll_goods_conversion,
    R.id.btn_goods_buy,R.id.btn_goods_cart})
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
                    RongIM.getInstance().startPrivateChat(this,thegoods.getUserid(),thegoods.getUsername());
                }
                break;
            case R.id.btn_goods_buy:
                break;
            case R.id.btn_goods_cart:
                SharedPreferences sharedP = getSharedPreferences("userinfo", MODE_PRIVATE);

                Goods goods  = new Goods();
                goods = thegoods;
                goods.setBuyerid(sharedP.getString("userid",""));
                goods.setBuyername(sharedP.getString("username",""));

                OkHttpUtils
                        .postString()
                        .url(getUrl()+url)
                        .mediaType(MediaType.parse("application/json; charset=utf-8"))
                        .content(new Gson().toJson(goods))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                L.i_crz("GoodsDetail post error:"+e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if(response.equals("true")){

                                }else{

                                }
                            }
                        });
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
