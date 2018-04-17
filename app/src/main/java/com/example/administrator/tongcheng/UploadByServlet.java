package com.example.administrator.tongcheng;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2017/10/27.
 */

public class UploadByServlet {private static String Url = "http://192.168.1.106:8080/Pap/";

    //传输商品信息
    public static String post(String url, NameValuePair... nameValuePairs){
        HttpClient httpClient = new DefaultHttpClient();
        String msg = "";
        HttpPost post = new HttpPost(Url+url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for(int i = 0; i<nameValuePairs.length;i++){
            params.add(nameValuePairs[i]);
        }
        try{
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = httpClient.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                msg = EntityUtils.toString(response.getEntity());
            }else{
                Log.e("crz","post网络请求失败");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();;
            msg = e.getMessage();
        }catch(IOException e){
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    //上传登录信息
}
