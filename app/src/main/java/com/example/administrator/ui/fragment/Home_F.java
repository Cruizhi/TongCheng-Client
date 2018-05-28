package com.example.administrator.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.ui.activity.ChooseCity;
import com.example.administrator.activity.GoodsList;
import com.example.administrator.activity.RecruitList;
import com.example.administrator.adapter.CarouselAdapter;
import com.example.administrator.http.UploadByServlet;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.ThreadPoolManager;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imageloader.utils.L;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/2/2.
 */

public class Home_F extends Fragment implements AMapLocationListener{
//    public static Home_F instance = null;
//
//    public static Home_F getInstance(){
//        if(instance == null){
//            instance = new Home_F();
//        }
//        return instance;
//    }
//
//    private Button Cuihua;
//    private Button Goudan;
//    private Button Zhuzi;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate){
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home,null);
//        init(view);
//        return view;
//    }
//
//    public void init(View view){
//        Cuihua = (Button)view.findViewById(R.id.cuihua);
//        Goudan = (Button)view.findViewById(R.id.goudan);
//        Zhuzi = (Button)view.findViewById(R.id.zhuzi);
//        Cuihua.setOnClickListener(this);
//        Goudan.setOnClickListener(this);
//        Zhuzi.setOnClickListener(this);
//    }


    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    @BindView(R.id.tv_top_city)
    TextView TvCity;  //当前城市
    @BindView(R.id.rpv_home_carousel)
    RollPagerView rollPagerView;   //首页旋转图片的框架
    @BindView(R.id.iv_home_recruit)
    ImageView IvRecruit;
    @BindView(R.id.iv_home_goods)
    ImageView IvGoods;
    @BindView(R.id.iv_home_tenement)
    ImageView IvTenement;
    @BindView(R.id.iv_home_pet)
    ImageView IvPet;
    @BindView(R.id.iv_home_car)
    ImageView IvCar;

    private String[] pic_url = new String[5];   //旋转图片的网络地址

    private CarouselAdapter carouselAdapter = new CarouselAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        //引入布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home,null);
        //设置全屏
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);

        ButterKnife.bind(this,view);

        ThreadPoolManager.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                BasicNameValuePair bnvp = new BasicNameValuePair("requestcode","1");
                String response = UploadByServlet.post("HomeTopPic",bnvp);
                Message msg = new Message();
                msg.obj = response;
                handler.sendMessage(msg);
            }
        });


        initLocation();
        com.example.administrator.utils.L.i_crz("Home_F--onCreateView");
        onRPVClick();   //旋转图片点击事件


        carouselAdapter.notifyDataSetChanged();

        return view;

    }

    private void initLocation(){

        mLocationClient = new AMapLocationClient(getActivity());
        mLocationClient.setLocationListener(this);
        //初始化定位参数声明AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(200000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        com.example.administrator.utils.L.i_crz("initLocation()");
    }

//    @Override
    @OnClick({R.id.tv_top_city,R.id.iv_home_goods,R.id.iv_home_recruit,R.id.iv_home_car,
            R.id.iv_home_tenement,R.id.iv_home_pet})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_city:
                Intent intent = new Intent(getActivity(), ChooseCity.class);
                startActivityForResult(intent,102);
                break;
            case R.id.iv_home_goods:
                Intent intent1 = new Intent(getActivity(), GoodsList.class);
                startActivity(intent1);
                break;
            case R.id.iv_home_recruit:
                Intent intent2 = new Intent(getActivity(), RecruitList.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        switch(requestCode){
            case 102:
                if(resultCode == 102){
                    String city = intent.getStringExtra("address");
                    TvCity.setText(city);
                    SharedPreferences sharePreferences = getActivity().getSharedPreferences("localcity",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharePreferences.edit();  //编辑SharePreferences
                    editor.putString("localcity",city);  //添加当前城市
                    editor.apply();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        com.example.administrator.utils.L.i_crz("Home_f--onLocationChanged");
        if(aMapLocation != null){
            if(aMapLocation.getErrorCode() == 0){
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码
                TvCity.setText(aMapLocation.getCity());
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                com.example.administrator.utils.L.i_crz("Home_F--onLocationChanged--city:"+aMapLocation.getCity());

                SharedPreferences sharePreferences = getActivity().getSharedPreferences("localcity",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharePreferences.edit();  //编辑SharePreferences
                editor.putString("localcity",aMapLocation.getCity());  //添加当前城市
                editor.apply();
            }
        }
    }

    private void onRPVClick(){
        rollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), "Item " + position + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            String response = (String)msg.obj;
            try{
                JSONArray ja = new JSONArray(response);
                for(int i = 0;i < ja.length(); i++){
                    JSONObject jo = ja.getJSONObject(i);
                    pic_url[0] = jo.getString("pic1");
                    pic_url[1] = jo.getString("pic2");
                    pic_url[2] = jo.getString("pic3");
                    pic_url[3] = jo.getString("pic4");
                    pic_url[4] = jo.getString("pic5");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            carouselAdapter.url(pic_url[0],pic_url[1],pic_url[2],pic_url[3],pic_url[4]);
            rollPagerView.setAdapter(carouselAdapter);
            L.i("crz","Handle--rollPagerView");
        }
    };
}
