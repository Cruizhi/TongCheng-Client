package com.example.administrator.adapter;

import android.content.Context;

import com.example.administrator.tongcheng.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/24.
 */

public class WelfareAdapter extends CommonAdapter<String> {

    public List<Boolean> isChecked = new ArrayList<Boolean>();

    public WelfareAdapter(Context context,List<String> datas,int itemLayoutId){
        super(context,datas,itemLayoutId);
        for(int i = 0; i < datas.size();i++){
            isChecked.add(false);
        }
    }

    @Override
    public void convert(ViewHolder viewHolder, String s) {
        viewHolder.setText(R.id.tv_welfare_item,s)
                .setCheckBox(R.id.cb_welfare,isChecked);
    }
}
