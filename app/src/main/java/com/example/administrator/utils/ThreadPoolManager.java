package com.example.administrator.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/3/18.
 */

public class ThreadPoolManager {

    private ExecutorService service;

    private ThreadPoolManager(){
        int num = Runtime.getRuntime().availableProcessors();  //获取cpu核心数
        service = Executors.newFixedThreadPool(num * 2);
    }

    private static ThreadPoolManager manager;

    //获取单例，保证只有一个线程池
    public static ThreadPoolManager getInstance(){
        if(manager == null){
            manager = new ThreadPoolManager();
        }
        return manager;
    }

    public void addTask(Runnable runnable){
        service.submit(runnable);   //接收并返回
    }

}
