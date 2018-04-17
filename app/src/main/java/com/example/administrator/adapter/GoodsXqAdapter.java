package com.example.administrator.adapter;

import android.content.Context;

import com.example.administrator.tongcheng.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public class GoodsXqAdapter extends CommonAdapter<String>{

    public GoodsXqAdapter(Context context, List<String> datas,int itemLayoutId){
        super(context,datas,itemLayoutId);
    }

    @Override
    public void convert(ViewHolder viewHolder, String s) {
        viewHolder.setImageURL(context,R.id.iv_goods_pics,s);
    }
}
