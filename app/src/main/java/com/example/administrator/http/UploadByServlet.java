package com.example.administrator.http;

import com.example.administrator.utils.L;

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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2018/1/26.
 */

public class UploadByServlet {

    private static String Url = "http://123.207.246.18:8080/TC/";
//    private static String Url = "http://192.168.1.111:8080/TC/";

    public static String getUrl() {
        return Url;
    }

    //上传信息到服务器
    public static String post(String url, NameValuePair... nameValuePairs){
        HttpClient httpClient = new DefaultHttpClient();
        String msg = "";
        HttpPost post = new HttpPost(Url+url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for(int i =0; i<nameValuePairs.length; i++){
            params.add(nameValuePairs[i]);
        }
        try{
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));  //防止中文乱码
            HttpResponse response = httpClient.execute(post);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                msg = EntityUtils.toString(response.getEntity(),"utf-8");
            }else{
                L.i_crz("post网络请求失败");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
            msg = e.getMessage();
        }catch(IOException e){
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    //上传图片
    public static void uploadimage(String imagepath,String imagename){
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try{
            URL url = new URL(Url+"UploadServlet");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection","Keep-Alive");
            conn.setRequestProperty("Charset","UTF-8");
            conn.setRequestProperty("Content-Type","multipart/from-data;boundary="+boundary);
            DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
            ds.writeBytes(twoHyphens+boundary+end);  //--*****\r\n
            ds.writeBytes("Content-Dispsition:form-data;"+"name=\"file\";filename=\""+imagename+"\""+end);
            ds.writeBytes(end);
            FileInputStream fStream = new FileInputStream(imagepath);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            while((length = fStream.read(buffer)) != -1){
                //从文件读取数据到缓冲区
                ds.write(buffer,0,length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            fStream.close();
            ds.flush();
            InputStream is = conn.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while((ch = is.read()) != -1){
                b.append((char) ch);
            }
            L.i_crz("uploadimage--上传成功");
            ds.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static final int TIME_OUT = 10*10000000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

//    public static String uploadimages(File file,String RequestURL){
//        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
//        String PREFIX = "--" , LINE_END = "\r\n";
//        String CONTENT_TYPE = "multipart/form-data";   //内容类型
//
//        try {
//            URL url = new URL(RequestURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(TIME_OUT);
//            conn.setConnectTimeout(TIME_OUT);
//            conn.setDoInput(true);  //允许输入流
//            conn.setDoOutput(true); //允许输出流
//            conn.setUseCaches(false);  //不允许使用缓存
//            conn.setRequestMethod("POST");  //请求方式
//            conn.setRequestProperty("Charset", CHARSET);  //设置编码
//            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//            if(file!=null)
//            {
//                /**
//                 * 当文件不为空，把文件包装并且上传
//                 */
//                OutputStream outputSteam=conn.getOutputStream();
//
//                DataOutputStream dos = new DataOutputStream(outputSteam);
//                StringBuffer sb = new StringBuffer();
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINE_END);
//                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+file.getName()+"\""+LINE_END);
//                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
//                sb.append(LINE_END);
//                dos.write(sb.toString().getBytes());
//                InputStream is = new FileInputStream(file);
//                byte[] bytes = new byte[1024];
//                int len = 0;
//                while((len=is.read(bytes))!=-1)
//                {
//                    dos.write(bytes, 0, len);
//                }
//                is.close();
//                dos.write(LINE_END.getBytes());
//                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
//                dos.write(end_data);
//                dos.flush();
//                //当响应成功时，获取响应流
//                int res = conn.getResponseCode();
//                L.i_crz("uploadimages--response code:"+res);
//                if(res == 200){
//                    return SUCCESS;
//                }
//            }
//        }catch(MalformedURLException e){
//            e.printStackTrace();
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//        return FAILURE;
//    }

    public static String uploadimages(File file, String RequestURL,String filename){
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if(file!=null)
            {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                OutputStream outputSteam=conn.getOutputStream();

                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+filename+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1)
                {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                //当响应成功时，获取响应流
                int res = conn.getResponseCode();
                L.i_crz("uploadimages--response code:"+res);
                if(res == 200){
                    return SUCCESS;
                }
            }
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return FAILURE;
    }
}
