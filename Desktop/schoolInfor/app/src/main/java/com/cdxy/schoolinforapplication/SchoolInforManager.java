package com.cdxy.schoolinforapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.util.Stack;

/**
 * Created by huihui on 2016/12/20.
 */

public class SchoolInforManager extends Application {
    private static Context context;
    private static Stack<Activity> activityStack;
    private static SchoolInforManager application;

public static SchoolInforManager getInstance(){
    if (application==null){
        application=new SchoolInforManager();
    }
    return application;
}
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }
    public static Context getContext(){
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}