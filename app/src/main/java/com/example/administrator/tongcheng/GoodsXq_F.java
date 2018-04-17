package com.example.administrator.tongcheng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.adapter.GoodsXqAdapter;
import com.example.administrator.bean.Goods;
import com.example.administrator.utils.L;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.http.UploadByServlet.getUrl;

/**
 * Created by Administrator on 2018/4/10.
 */

public class GoodsXq_F extends Fragment {

    private ListView LvPics;

    private Goods TheGoods;
    private List<String> picsList = new ArrayList<String>();
    private GoodsXqAdapter myAdapter;

    private String[] pics_url = new String[7];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_goods_xq,container,false);

        init(view);

        return view;
    }

    private void init(View view){
        LvPics = (ListView)view.findViewById(R.id.lv_goods_pics);

        Bundle bundle = getActivity().getIntent().getExtras();
        TheGoods = (Goods)bundle.getSerializable("GoodsObj");

        pics_url[0] = getUrl()+"images/"+TheGoods.getPic0();
        pics_url[1] = getUrl()+"images/"+TheGoods.getPic1();
        pics_url[2] = getUrl()+"images/"+TheGoods.getPic2();
        pics_url[3] = getUrl()+"images/"+TheGoods.getPic3();
        pics_url[4] = getUrl()+"images/"+TheGoods.getPic4();
        pics_url[5] = getUrl()+"images/"+TheGoods.getPic5();
        pics_url[6] = getUrl()+"images/"+TheGoods.getPic6();

        L.i_crz("pics_url--length:"+pics_url.length);
        for(int i = 0; i <= 6;i++){
            picsList.add(pics_url[i]);
        }

        myAdapter = new GoodsXqAdapter(getActivity(),picsList,R.layout.item_goods_picture);
        LvPics.setAdapter(myAdapter);
        L.i_crz("picsList-size"+picsList.size());

    }

}
