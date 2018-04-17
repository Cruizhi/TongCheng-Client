package com.example.administrator.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.administrator.http.ReceiveByServlet;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ReceiveTask extends AsyncTask<String,Void,Bitmap>{

    private LruCache<String,Bitmap> mLruCaches;   //使用LruCache缓存，节省流量，加载速度快
    //设置缓存
    int maxMemory = (int)Runtime.getRuntime().maxMemory();
    int cacheSize = maxMemory/4;

    private ImageView imageView;
    public ReceiveTask(ImageView imageview){
        this.imageView = imageview;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        super.onPostExecute(bitmap);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params){
        mLruCaches = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key,Bitmap value){
                return value.getByteCount();
            }
        };
        Bitmap bitmap = new ReceiveByServlet().getPicture(params[0]);
        if(bitmap != null){
            addBitmapToCache(params[0],bitmap);
        }
        L.i_crz("ReceiveTask--doInBackground");
        return bitmap;
    }

    //将图片添加到缓存中
    private void addBitmapToCache(String url,Bitmap bitmap){
        //若没有缓存，则添加到缓存
        if(getBitmapFromCache(url) == null){
            mLruCaches.put(url,bitmap);
        }
    }

    //从缓存获取图片
    private Bitmap getBitmapFromCache(String url){
        return mLruCaches.get(url);
    }
}
