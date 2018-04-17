package com.example.administrator.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.administrator.bean.Goods;
import com.example.administrator.tongcheng.R;
import com.example.administrator.widget.GoodsItem;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class GoodsItemAdapter extends BaseAdapter {

    private List<Goods> mList;
    private Context mContext;
    private int sumCount;
    private ChangeTextView changeTextView;

    public Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    changeTextView.changeTheText(msg.arg1);
                    break;
                default:
                    break;

            }
        }
    };

    public GoodsItemAdapter(List<Goods> mList,Context context,ChangeTextView changeTextView){
        this.mList = mList;
        this.mContext = context;
        this.changeTextView = changeTextView;
    }

    @Override
    public int getCount() {
        int count = mList.size();
        if(count % 2 == 0 ){
            sumCount = count/2;
        }else{
            sumCount = (int)Math.floor((double)count / 2) + 1;
        }
        return sumCount;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder{
        GoodsItem goodsItem1;
        GoodsItem goodsItem2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_list,null);
            holder.goodsItem1 = (GoodsItem)convertView.findViewById(R.id.gi_goods_item1);
            holder.goodsItem2 = (GoodsItem)convertView.findViewById(R.id.gi_goods_item2);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.goodsItem1.setTitle(mList.get(position * 2).getTitle());
        holder.goodsItem1.setPrice(mList.get(position * 2).getPrice());
        holder.goodsItem1.setPic(mList.get(position * 2).getPic0(),mContext);

        if(position * 2+1 == mList.size()){
            holder.goodsItem2.setVisibility(View.INVISIBLE);
        }else{
            holder.goodsItem2.setVisibility(View.VISIBLE);
            holder.goodsItem2.setTitle(mList.get(position * 2+1).getTitle());
            holder.goodsItem2.setPrice(mList.get(position * 2+1).getPrice());
            holder.goodsItem2.setPic(mList.get(position * 2+1).getPic0(),mContext);
        }

        holder.goodsItem1.setMyItemClickedListener(new MyOnEvenClick(position));
        holder.goodsItem2.setMyItemClickedListener(new MyOnOddClick(position));
        return convertView;
    }

    private class MyOnEvenClick implements GoodsItem.MyItemClicked {
        int pos = 0;

        public MyOnEvenClick(int position) {
            this.pos = position * 2;
        }

        @Override
        public void myItemClicked() {
            Message message = new Message();
            message.what = 1;
            message.arg1 = pos;
            handler.sendMessage(message);
        }

    }

    private class MyOnOddClick implements GoodsItem.MyItemClicked {
        int pos = 0;

        public MyOnOddClick(int position) {
            this.pos = position * 2 + 1;
        }

        @Override
        public void myItemClicked() {
            Message message = new Message();
            message.what = 1;
            message.arg1 = pos;
            handler.sendMessage(message);
        }

    }

    // 设置监听
    public interface ChangeTextView {
        void changeTheText(int position);
    }
}
