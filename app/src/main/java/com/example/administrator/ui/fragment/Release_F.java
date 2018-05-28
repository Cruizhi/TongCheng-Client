package com.example.administrator.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.ui.activity.ChooseType;
import com.example.administrator.tongcheng.R;
import com.example.administrator.utils.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/2.
 */

public class Release_F extends Fragment{

    @BindView(R.id.iv_release_tenement)
    ImageView IvTenement;  //租房按钮
    @BindView(R.id.iv_release_recruit)
    ImageView IvRecruit;  //招聘按钮
    @BindView(R.id.iv_release_curriculum_vitae)
    ImageView IvCurriculum;  //发布简历按钮
    @BindView(R.id.iv_release_goods)
    ImageView IvGoods;  //二手物品按钮
    @BindView(R.id.iv_release_car)
    ImageView IvCar;  //二手车按钮
    @BindView(R.id.iv_release_pet)
    ImageView IvPet;  //宠物按钮

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //引入布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_release,null);
        //设置全屏
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);

        L.i_crz("Release_F--onCreateView");
        ButterKnife.bind(this,view);

        return view;
    }

//    @Override
    @OnClick({R.id.iv_release_tenement,R.id.iv_release_recruit,R.id.iv_release_curriculum_vitae,
            R.id.iv_release_goods,R.id.iv_release_car,R.id.iv_release_pet})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_release_tenement:
                Intent intent0 = new Intent(getActivity(), ChooseType.class);
                intent0.putExtra("type","tenement");
                startActivity(intent0);
                break;
            case R.id.iv_release_recruit:
                Intent intent1 = new Intent(getActivity(),ChooseType.class);
                intent1.putExtra("type","recruit");
                startActivity(intent1);
                break;
            case R.id.iv_release_curriculum_vitae:
                Intent intent2 = new Intent(getActivity(),ChooseType.class);
                intent2.putExtra("type","curriculum");
                startActivity(intent2);
                break;
            case R.id.iv_release_goods:
                Intent intent3 = new Intent(getActivity(),ChooseType.class);
                intent3.putExtra("type","goods");
                startActivity(intent3);
                break;
            case R.id.iv_release_car:
                Intent intent4 = new Intent(getActivity(),ChooseType.class);
                intent4.putExtra("type","car");
                startActivity(intent4);
                break;
            case R.id.iv_release_pet:
                Intent intent5 = new Intent(getActivity(),ChooseType.class);
                intent5.putExtra("type","pet");
                startActivity(intent5);
                break;
            default:
                break;
        }
    }
}
