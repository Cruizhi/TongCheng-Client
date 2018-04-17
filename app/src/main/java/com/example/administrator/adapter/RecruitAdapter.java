package com.example.administrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.bean.Recruit;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class RecruitAdapter extends BaseAdapter {

    private List<Recruit> recruitList;
    private LayoutInflater layoutInflater;

    public RecruitAdapter(Context context, List<Recruit> datas) {
        this.recruitList = datas;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return recruitList.size();
    }

    @Override
    public Object getItem(int position) {
        return recruitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Recruit recruit = recruitList.get(position);
        String recruittype = recruit.getRecruittype();
        L.i_crz("recruitadapter--recruittype--:"+recruittype);
        ViewHolder viewHolder = null;
        if(convertView == null){
            if (recruittype.equals("parttime")){
                convertView = layoutInflater.inflate(R.layout.item_parttime,null);
                viewHolder = new ViewHolder();
                viewHolder.TvTitle = (TextView)convertView.findViewById(R.id.tv_parttime_item_title);
                viewHolder.TvWages = (TextView)convertView.findViewById(R.id.tv_parttime_item_wages);
                viewHolder.TvCity = (TextView)convertView.findViewById(R.id.tv_parttime_item_city);
                viewHolder.TvDuration = (TextView)convertView.findViewById(R.id.tv_parttime_item_duration);
                viewHolder.TvDate = (TextView)convertView.findViewById(R.id.tv_parttime_item_date);
                viewHolder.TvType = (TextView)convertView.findViewById(R.id.tv_parttime_item_type);
            }else{
                convertView = layoutInflater.inflate(R.layout.item_fulltime,null);
                viewHolder = new ViewHolder();
                viewHolder.TvTitle = (TextView)convertView.findViewById(R.id.tv_fulltime_item_title);
                viewHolder.TvWages = (TextView)convertView.findViewById(R.id.tv_fulltime_item_wages);
                viewHolder.TvCity = (TextView)convertView.findViewById(R.id.tv_fulltime_item_city);
                viewHolder.TvWelfare = (TextView)convertView.findViewById(R.id.tv_fulltime_item_welfare);
                viewHolder.TvType = (TextView)convertView.findViewById(R.id.tv_fulltime_item_type);
            }
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(recruittype.equals("parttime")){
            viewHolder.TvTitle.setText(recruit.getName());
            viewHolder.TvWages.setText(recruit.getWages());
            viewHolder.TvCity.setText(recruit.getCity());
            viewHolder.TvType.setText("兼职");
            viewHolder.TvDate.setText(recruit.getDate());
            viewHolder.TvDuration.setText(recruit.getDuration());
        }else{
            viewHolder.TvWelfare.setText(recruit.getWelfare());
            viewHolder.TvTitle.setText(recruit.getName());
            viewHolder.TvWages.setText(recruit.getWages());
            viewHolder.TvCity.setText(recruit.getCity());
            viewHolder.TvType.setText("全职");
        }




        return convertView;
    }

    private static class ViewHolder{
        public TextView TvTitle;
        public TextView TvWages;
        public TextView TvCity;
        public TextView TvType;
        public TextView TvWelfare;
        public TextView TvDate;
        public TextView TvDuration;
    }
}
