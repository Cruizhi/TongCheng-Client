package com.example.administrator.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.administrator.bean.Goods;
import com.example.administrator.bean.Recruit;
import com.example.administrator.bean.User;
import com.example.administrator.utils.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/27.
 */

public class ReceiveByServlet {

    private static String Url = "http://123.207.246.18:8080/TC/";
//    private static String Url = "http://192.168.1.111:8080/TC/";

    //访问服务器获取数据库数据
    public String getByServlet(String url){
        String str = "";
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(Url + url);
            HttpResponse response = httpClient.execute(post);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                str = EntityUtils.toString(entity,"utf-8");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return str;
    }

    //获取当前登录用户信息
    public static User getUserinformation(String response){
        User user = new User();
        try{
            JSONArray ja = new JSONArray(response);
            JSONObject jo = ja.getJSONObject(0);
            user.setUserid(jo.getString("phone"));
            user.setPhone(jo.getString("phone"));
            user.setPassword(jo.getString("password"));
            user.setUsername(jo.getString("username"));
            user.setEmail(jo.getString("email"));
            user.setSex(jo.getString("sex"));
            user.setAddress(jo.getString("address"));
            user.setPic(jo.getString("pic"));
            user.setToken(jo.getString("token"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return  user;
    }

    //获取商品列表
    public List<Goods> getGoodsList(){
        List<Goods> mylist = new ArrayList<Goods>();
        String response = getByServlet("GoodsList"); //获取list
        try{
            //JSONArray ja = JSONArray.fromObject(response);
            JSONArray ja = new JSONArray(response);
            for(int i = 0;i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Goods goods = new Goods();
                goods.setId(jo.getInt("id"));
                goods.setUserid(jo.getString("userid"));
                goods.setToken(jo.getString("token"));
                goods.setType(jo.getString("type"));
                goods.setTitle(jo.getString("title"));
                goods.setContent(jo.getString("content"));
                goods.setPrice(jo.getString("price"));
                goods.setAssortment(jo.getString("assortment"));
                goods.setLinkphone(jo.getString("linkphone"));
                goods.setCarriage(jo.getString("carriage"));
                goods.setCity(jo.getString("city"));
                goods.setPic0(jo.getString("pic0"));
                goods.setPic1(jo.getString("pic1"));
                goods.setPic2(jo.getString("pic2"));
                goods.setPic3(jo.getString("pic3"));
                goods.setPic4(jo.getString("pic4"));
                goods.setPic5(jo.getString("pic5"));
                goods.setPic6(jo.getString("pic6"));
                mylist.add(goods);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return mylist;
    }

    //获取招聘列表
    public List<Recruit> getRecruitList(){
        List<Recruit> mylist = new ArrayList<Recruit>();
        String response = getByServlet("RecruitList"); //获取list
        try{
            //JSONArray ja = JSONArray.fromObject(response);
            JSONArray ja = new JSONArray(response);
            for(int i = 0;i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Recruit recruit = new Recruit();
                recruit.setUserid(jo.getString("userid"));
                recruit.setRecruittype(jo.getString("recruittype"));
                recruit.setType(jo.getString("type"));
                recruit.setName(jo.getString("name"));
                recruit.setNumber(jo.getString("number"));
                recruit.setDuration(jo.getString("duration"));
                recruit.setPeriod(jo.getString("period"));
                recruit.setWages(jo.getString("wages"));
                recruit.setPayroll(jo.getString("payroll"));
                recruit.setContent(jo.getString("content"));
                recruit.setAddress(jo.getString("address"));
                recruit.setWelfare(jo.getString("welfare"));
                recruit.setLinkman(jo.getString("linkman"));
                recruit.setLinkphone(jo.getString("linkphone"));
                recruit.setCompany(jo.getString("company"));
                recruit.setCity(jo.getString("city"));
                recruit.setDate(jo.getString("date"));
                recruit.setToken(jo.getString("token"));
                mylist.add(recruit);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return mylist;
    }

    //从服务器接收首页旋转的图片
    public Bitmap getPicture(String urlpath){
        L.i_crz("ReceiveServlet--getPicture");
        Bitmap bm = null;
        InputStream is;
        try{
            URL url = new URL(urlpath);
            URLConnection connection = url.openConnection();
            connection.connect();
            is = connection.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;  //图片缩小为原来的四分之一
            bm = BitmapFactory.decodeStream(is,null,options);
            is.close();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return bm;
    }

}
