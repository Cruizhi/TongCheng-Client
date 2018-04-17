package com.example.administrator.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {

    protected List<T> datas = null;  //数据源
    protected Context context = null;  //上下文对象
    protected int itemLayoutResId = 0;  //item布局文件资源

    public CommonAdapter(Context context,List<T> datas,int itemLayoutResId){
        this.context = context;
        this.datas = datas;
        this.itemLayoutResId = itemLayoutResId;
    }

    @Override
    public int getCount(){
        return datas.size();
    }

    @Override
    public T getItem(int position){
        return datas.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder = ViewHolder.get(context,convertView,parent,itemLayoutResId,position);
        convert(viewHolder,getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder viewHolder,T t);

}
