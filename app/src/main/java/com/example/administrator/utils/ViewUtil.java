package com.example.administrator.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
/**
 * Created by Administrator on 2018/5/2.
 */

public class ViewUtil {

    public static void hideKeyBoard(Context context, ViewGroup view){
        view.requestFocus();
        InputMethodManager im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        try{
            im.hideSoftInputFromWindow(view.getWindowToken(),0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void showKeyBoard(Context context,View view){
        InputMethodManager im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view,0);
    }

    public static void toast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

}
