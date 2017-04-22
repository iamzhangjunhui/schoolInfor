package com.cdxy.schoolinforapplication.ui.Message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.cdxy.schoolinforapplication.model.message.MessageEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送消息处理类
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private  Handler handler;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String nowTime= format.format(date);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //获得接受消息设备注册ID

        }
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
            Intent i = new Intent(context, MessageDetailActivity.class);
            MessageEntity messageEntity=new MessageEntity();
            messageEntity.setTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
            messageEntity.setContent(bundle.getString(JPushInterface.EXTRA_ALERT));
            messageEntity.setSendTime(nowTime);
            i.putExtra("message",messageEntity);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity(i);

        }


    }

    // 打印所有的 intent extra 数据




}
