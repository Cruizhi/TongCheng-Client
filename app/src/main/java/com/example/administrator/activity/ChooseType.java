package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.adapter.TypeAdapter;
import com.example.administrator.bean.Type;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class ChooseType extends Activity {

    public final String[] intentdatas = {"tenement","recruit","curriculum","goods","car","pet"};
    private final String[] recruit = {"全职招聘","兼职招聘"};
    private Class[] recruitcontext = {FullTimeJob.class, PartTimeJob.class};
    private final String[] goods = {"商家发布","个人发布"};
    private Class[] goodscontext = {BusinessRelease.class,PersonalRelease.class};
    private String mType;  //获取类型

    private TypeAdapter myAdapter;
    private List<Type> typeList = new ArrayList<Type>();  //初始化list列表

    private ListView listView;
    private Button BtBack;

    @Override
    protected void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_public_choose);

        getData();  //获取从上一个Activity传过来的数据

        init();  //初始化
        initClick();
    }

    private void getData(){
        //获取上一个activity传过来的数据
        Intent intent = getIntent();
        if(intent != null){
            mType = intent.getStringExtra("type");
        }
    }

    private void init(){
        BtBack = (Button)findViewById(R.id.btn_choosetype_back);
        listView = (ListView)findViewById(R.id.lv_choosetype_chooseitem);

        initView();  //初始化界面
    }

    private void initView(){
        switch (mType){
            case "tenement":
                break;
            case "recruit":
                for(int i = 0;i <= 1;i++ ){
                    Type type1 = new Type();
                    type1.setType(recruit[i]);
                    type1.setContext(recruitcontext[i]);
                    typeList.add(type1);
                }
                myAdapter = new TypeAdapter(ChooseType.this,typeList,R.layout.item_public_choose);
                break;
            case "goods":
                for(int i = 0;i <= 1;i++){
                    Type type2 = new Type();
                    type2.setType(goods[i]);
                    type2.setContext(goodscontext[i]);
                    typeList.add(type2);
                }
                myAdapter = new TypeAdapter(ChooseType.this,typeList,R.layout.item_public_choose);
                break;
            default:
                break;
        }
        listView.setAdapter(myAdapter);
    }

    private void initClick(){
        BtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Type thetype = typeList.get(position);
                L.i_crz("ClickItem--listview ");
//              thetype.getIntentActvity().replace("\"","").getClass();Class.forName(thetype.getIntentActvity())
                Intent intent = new Intent(ChooseType.this,thetype.getContext());
                startActivity(intent);
                finish();
            }
        });
    }
}
