package com.example.administrator.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.administrator.http.UploadByServlet;

import java.io.File;

import static com.example.administrator.http.UploadByServlet.getUrl;

/**
 * Created by Administrator on 2018/3/12.
 * 异步传输图片
 * params 启动任务执行的输入参数
 * progress 后台执行任务的进度
 * result 后台计算结果的类型
 *
 */

public class UploadTask extends AsyncTask<String,Void,String> {

    public static final String requestURL = "ReceiveImages";

    private String filename;
    private String newfilename = null;

    private ProgressDialog pdialog;
    private Activity context = null;

    public UploadTask(Activity context,String newfilename){
        this.context = context;
        this.newfilename = newfilename;
        pdialog = ProgressDialog.show(context,"正在加载......","系统正在处理你请求");
    }

    /*
     *当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
     */
    @Override
    protected void onPostExecute(String result){
        //返回HTML页面的内容
        pdialog.dismiss();  //关闭
        if(UploadByServlet.SUCCESS.equalsIgnoreCase(result)){
            Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"上传失败",Toast.LENGTH_SHORT).show();
        }
    }

    /*
     *在execute(Params... params)被调用后立即执行，一般用来在执行后台任务前对UI做一些标记。
     */
    @Override
    protected void onPreExecute(){
    }

    /*
     *取消
     */
     @Override
     protected void onCancelled(){
         super.onCancelled();
     }

    /*
     *在onPreExecute()完成后立即执行，用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。
     * 在执行过程中可以调用publishProgress(Progress... values)来更新进度信息。
     */
    @Override
    protected String doInBackground(String... params) {
        File file = new File(params[0]);
        filename = file.getName();
        filename = filename.substring(filename.lastIndexOf("\\")+1);    //获取文件名
        return UploadByServlet.uploadimages(file,getUrl()+requestURL,newfilename);
    }

    /*
     *在调用publishProgress(Progress... values)时，此方法被执行，直接将进度信息更新到UI组件上。
     */
    @Override
    protected void onProgressUpdate(Void... values){
    }
}
