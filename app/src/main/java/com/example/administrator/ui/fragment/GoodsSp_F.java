package com.example.administrator.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.adapter.CarouselAdapter;
import com.example.administrator.bean.Goods;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;
import com.jude.rollviewpager.RollPagerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.http.UploadByServlet.getUrl;

/**
 * Created by Administrator on 2018/4/10.
 */

public class GoodsSp_F extends Fragment {

    @BindView(R.id.rpv_goods_carousel)
    RollPagerView RpvGoodspic;
    @BindView(R.id.tv_goods_price)
    TextView TvPrice;
    @BindView(R.id.tv_goods_content)
    TextView TvContent;
    @BindView(R.id.tv_goods_city)
    TextView TvCity;
    @BindView(R.id.tv_goods_carriage)
    TextView TvCarriage;
    @BindView(R.id.tv_goods_type)
    TextView TvType;

    private Goods Thegoods;
    private CarouselAdapter myAdapter  = new CarouselAdapter();

    private String[] pic_url = new String[7];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_goods_sp,container,false);

        ButterKnife.bind(this,view);
        init(view);
        L.i_crz("pics_url"+pic_url[0]+pic_url[1]);
        myAdapter.url(pic_url[0],pic_url[1]);
        RpvGoodspic.setAdapter(myAdapter);
        return view;
    }

    private void init(View view){
        Bundle bundle = getActivity().getIntent().getExtras();
        Thegoods = (Goods)bundle.getSerializable("GoodsObj");
        TvPrice.setText("￥"+Thegoods.getPrice());
        TvContent.setText(Thegoods.getContent());
        TvCity.setText(Thegoods.getCity());
        TvCarriage.setText(Thegoods.getCarriage());

        pic_url[0] = getUrl()+"images/"+Thegoods.getPic0();
        pic_url[1] = getUrl()+"images/"+Thegoods.getPic1();
        pic_url[2] = getUrl()+"images/"+Thegoods.getPic2();

        if(Thegoods.getType().equals("business")){
            TvType.setText("商家");
        }else{
            TvType.setText("个人");
        }

    }
}
