package com.example.administrator.adapter;

import android.content.Context;

import com.example.administrator.bean.Type;
import com.example.administrator.tongcheng.R;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class TypeAdapter extends CommonAdapter<Type> {

    public TypeAdapter(Context context, List<Type> datas, int itemLayoutResId){
        super(context,datas,itemLayoutResId);
    }

    @Override
    public void convert(ViewHolder viewHolder,Type type) {
        viewHolder.setText(R.id.tv_choose_choosetype,type.getType());
    }
}
