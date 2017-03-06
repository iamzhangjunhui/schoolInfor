package com.cdxy.schoolinforapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by huihui on 2017/3/6.
 */

public class SharedPreferenceManager {
    private static final String NAME = "school_infor_db";
    private static SharedPreferenceManager sharedPreferenceManager;
    private Context context;
    public static final String LOGIN_NAME="loginName";
    public static  final String PASSWORD="password";

    public SharedPreferenceManager(Context context) {
        this.context = context;
    }

    public static SharedPreferenceManager instance(Context context) {
        if (sharedPreferenceManager == null) {
            sharedPreferenceManager = new SharedPreferenceManager(context);
        }
        return sharedPreferenceManager;
    }

    public SharedPreferences.Editor getEditor() {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        return editor;
    }
    public SharedPreferences getSharedPreferences(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
