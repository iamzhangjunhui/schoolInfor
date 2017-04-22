package com.cdxy.schoolinforapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.wxlib.util.SysUtil;
import com.cdxy.schoolinforapplication.ui.chat.ConversationListOperationCustomSample;
import com.cdxy.schoolinforapplication.ui.chat.ConversationListUICustomSample;

import java.util.Stack;

/**
 * Created by huihui on 2016/12/20.
 */

public class SchoolInforManager extends MultiDexApplication {
    private static Context context;
    private static SchoolInforManager application;
    public static String appKay = "23666123";

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
        SysUtil.setApplication(this);
        //必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        } else if (SysUtil.isMainProcess()) {
            //第一个参数是Application Context
            //这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
            YWAPI.init(this, appKay);
        }
        //阿里云自定义会话列表的相关类。
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationListUICustomSample.class);
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_OPERATION_POINTCUT, ConversationListOperationCustomSample.class);
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