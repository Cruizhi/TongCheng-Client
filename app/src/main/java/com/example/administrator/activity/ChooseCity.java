package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.RegionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/5.
 */

public class ChooseCity extends Activity {

    private RegionUtils region = new RegionUtils();

    private Spinner SpProvince;
    private Spinner SpCity;

    private Button Btconfirm;
    private int resultCode = 102;

    private JSONObject jsonObject;
    private String[] allprovince;

    private ArrayAdapter<String> provinceAdpater;
    private ArrayAdapter<String> cityAdapter;

    private String[] allSpinList;

    private String address;

    private String provinceName;
    private Boolean isFirstLoad = true;

    private Map<String,String[]> cityMap = new HashMap<String,String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosecity);
        initJsonData();  //初始化json数据
        initDatas();  //初始化省市数据
        initView();  //初始化控件
        initClick();
        setSpinnerData();  //为spinner设置值
    }

    private void setSpinnerData(){
        int selectPosition = 0;
        address = getIntent().getStringExtra("address");
        if(address != null && address.equals("")){
            allSpinList = address.split(" ");
        }

        provinceAdpater = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);  //系统自带
        for(int i = 0; i < allprovince.length; i++){
            if(address != null && !address.equals("")){
                selectPosition = i;
            }
            provinceAdpater.add(allprovince[i]);
        }
        provinceAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  //按下的效果
        SpProvince.setAdapter(provinceAdpater);
        SpProvince.setSelection(selectPosition);

        cityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpCity.setAdapter(cityAdapter);

        setListener();
    }

    private void setListener(){
        SpProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                provinceName = parent.getSelectedItem() + "";
                if(isFirstLoad){
                    if(address != null && !address.equals("") && allSpinList.length > 1 && allSpinList.length <3){
                        updateCityAndArea(provinceName,allSpinList[1],null);
                    }else if(address != null && !address.equals("") && allSpinList.length >= 3){
                        updateCityAndArea(provinceName,allSpinList[1],allSpinList[2]);
                    }else{
                        updateCityAndArea(provinceName,null,null);
                    }
                }else{
                    updateCityAndArea(provinceName,null,null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void updateCityAndArea(Object object,Object city,Object area){
        int selectPosition = 0;
        String[] cities = cityMap.get(object);
        cityAdapter.clear();
        for(int i = 0; i<cities.length; i++){
            if(city!=null && city.toString().equals(cities[i])){
                selectPosition = i;
            }
            cityAdapter.add(cities[i]);
        }

    }

    //初始化省市区数据
    private void initDatas() {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("citylist");//获取整个json数据
            allprovince = new String[jsonArray.length()];//封装数据
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonP = jsonArray.getJSONObject(i);//jsonArray转jsonObject
                String provStr = jsonP.getString("p");//获取所有的省
                allprovince[i] = provStr;//封装所有的省
                JSONArray jsonCity = null;

                try {
                    jsonCity = jsonP.getJSONArray("c");//在所有的省中取出所有的市，转jsonArray
                } catch (Exception e) {
                    continue;
                }
                //所有的市
                String[] allCity = new String[jsonCity.length()];//所有市的长度
                for (int c = 0; c < jsonCity.length(); c++) {
                    JSONObject jsonCy = jsonCity.getJSONObject(c);//转jsonObject
                    String cityStr = jsonCy.getString("n");//取出所有的市
                    allCity[c] = cityStr;//封装市集合

                    JSONArray jsonArea = null;
                    try {
                        jsonArea = jsonCy.getJSONArray("a");//在从所有的市里面取出所有的区,转jsonArray
                    } catch (Exception e) {
                        continue;
                    }
                    String[] allArea = new String[jsonArea.length()];//所有的区
                    for (int a = 0; a < jsonArea.length(); a++) {
                        JSONObject jsonAa = jsonArea.getJSONObject(a);
                        String areaStr = jsonAa.getString("s");//获取所有的区
                        allArea[a] = areaStr;//封装起来
                    }
                }
                cityMap.put(provStr, allCity);//某个省取出所有的市,
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonObject = null;//清空所有的数据
    }

    /**
     * 从assert文件夹中获取json数据
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getAssets().open("city.json");//打开json数据
            byte[] by = new byte[is.available()];//转字节
            int len = -1;
            while ((len = is.read(by)) != -1) {
                sb.append(new String(by, 0, len, "gb2312"));//根据字节长度设置编码
            }
            is.close();//关闭流
            jsonObject = new JSONObject(sb.toString());//为json赋值
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化点击
    private void initClick() {
        Btconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把值返回给MainActivity
                Intent intent = new Intent();
                intent.putExtra("address",SpCity.getSelectedItem().toString());
                setResult(resultCode, intent);
                finish();
            }
        });
    }

    private void initView(){
        SpProvince = (Spinner)findViewById(R.id.sp_choosecity_province);
        SpCity = (Spinner)findViewById(R.id.sp_choosecity_city);
        Btconfirm = (Button)findViewById(R.id.btn_choosecity_confirm);

    }

}
