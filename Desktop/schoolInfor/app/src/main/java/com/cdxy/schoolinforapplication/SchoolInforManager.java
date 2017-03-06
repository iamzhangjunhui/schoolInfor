package com.cdxy.schoolinforapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.wxlib.util.SysUtil;
import com.cdxy.schoolinforapplication.ui.chat.ConversationListUICustomSample;

import java.util.Stack;

/**
 * Created by huihui on 2016/12/20.
 */

public class SchoolInforManager extends Application {
    private static Context context;
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
        //阿里云旺的初始化
        String appKay = "23015524";
        SysUtil.setApplication(this);
        //必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        } else if (SysUtil.isMainProcess()) {
            //第一个参数是Application Context
            //这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
            YWAPI.init(this, appKay);
        }
        //在AppOncreate中注册自定义类，第一个参数表明自定义类型，第二个参数是自定义类的class
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationListUICustomSample.class);
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