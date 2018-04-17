package com.example.administrator.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.ReceiveTask;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import static com.mob.tools.gui.BitmapProcessor.getBitmapFromCache;

/**
 * Created by Administrator on 2018/3/16.
 */

public class CarouselAdapter extends StaticPagerAdapter{

    private String[] pic_path = new String[5];
    public void url(String... params){
        //遍历所有参数
        for(int i = 0;i < params.length; i++){
            pic_path[i] = params[i];
        }
    }

    @Override
    public View getView(ViewGroup container, int position){
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(R.drawable.img3);   //先加载一个默认图片,当网络不给力时，过渡
        Bitmap bitmap = getBitmapFromCache(pic_path[position]);
        if(bitmap != null){
            view.setImageBitmap(bitmap);   //如果缓存有图片，直接加载
        }else{
            //若无图片，异步加载
            ReceiveTask receiveTask = new ReceiveTask(view);
            receiveTask.execute(pic_path[position]);
        }
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);   //裁剪图片
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount(){
        return pic_path.length;
    }
}
