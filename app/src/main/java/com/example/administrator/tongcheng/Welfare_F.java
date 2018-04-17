package com.example.administrator.tongcheng;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.administrator.adapter.WelfareAdapter;
import com.example.administrator.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/24.
 */

public class Welfare_F extends DialogFragment implements View.OnClickListener {

    private GridView GvWelfare;
    private Button BtSubmit;

    private String[] welfare = {"五险一金","包吃","包住","周末双休","年底双薪","房补","饭补","交通补助"};
    private String allwelfare = "";
    private List<String> welfareList = new ArrayList<String>();
    private List<Integer> listitemID = new ArrayList<Integer>();

    private WelfareAdapter myAdapter;

    public OnDialogListener mlistener;

//    @Override
//    public void onStart(){
//        super.onStart();
//        //设置fragment在底部
//        Window window = getDialog().getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.gravity = Gravity.BOTTOM;
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(params);
//        //设置背景透明
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        //加这句话去掉自带的标题栏
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view = inflater.inflate(R.layout.dialogfragment_person, null);
//        //init(view);
//        return view;
//    }

    public interface OnDialogListener {
        void onDialogClick(String person);
    }
    public void setOnDialogListener(OnDialogListener dialogListener){
        this.mlistener = dialogListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState){
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_welfare);
        dialog.setCanceledOnTouchOutside(true);
        final Window window = dialog.getWindow();
        final WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM; // 紧贴底部
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        params.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(params);

        init(dialog);

        return dialog;
    }

    private void init(Dialog dialog){
        GvWelfare = (GridView)dialog.findViewById(R.id.gv_welfare_welfare);

        for(int i = 0;i < welfare.length; i++){
            welfareList.add(welfare[i]);
        }
        myAdapter = new WelfareAdapter(getActivity(),welfareList,R.layout.item_welfare);
        GvWelfare.setAdapter(myAdapter);
        GvWelfare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listitemID.clear();
                for(int i = 0;i<myAdapter.isChecked.size();i++){
                    if(myAdapter.isChecked.get(i)){
                        listitemID.add(i);
                    }
                }
                for(int i = 0;i < listitemID.size();i++){
                    allwelfare += welfareList.get(listitemID.get(i)) + " ";
                    L.i_crz("Welfare--allwelfare--"+allwelfare);
                }
            }
        });

        BtSubmit = (Button)dialog.findViewById(R.id.btn_welfare_submit);
        BtSubmit.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_welfare_submit:
                mlistener.onDialogClick(allwelfare);
                dismiss();
                break;
            default:
                break;
        }
    }
}
