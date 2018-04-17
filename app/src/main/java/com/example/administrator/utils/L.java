package com.example.administrator.utils;

import android.util.Log;

/**
 * Created by Administrator on 2018/1/26.
 * Log类
 */

public class L {
    private static boolean DE_BUG = true;
    private static String TAG = "crz";

    public static void i_crz(String text){
        if(DE_BUG)
            Log.i(TAG,text);
    }

    public static void e_crz(String text){
        if(DE_BUG)
            Log.e(TAG,text);
    }
}
