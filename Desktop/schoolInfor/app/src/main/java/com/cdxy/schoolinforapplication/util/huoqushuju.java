package com.cdxy.schoolinforapplication.util;

import android.util.Log;

import com.cdxy.schoolinforapplication.model.UserInfor.UserInforEntity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by su133 on 2017/3/1.
 */

//这边需要带到副线程
public class huoqushuju {

    public UserInforEntity huoqushuju(final String loginName) {
        final UserInforEntity userInforEntity = new UserInforEntity();
        Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                OkHttpClient okHttpClient = new OkHttpClient();
                Log.i("url", "http://192.168.191.1:8080/schoolinfor/huoqushuju?lianxi=" + loginName);
                Request request = new Request.Builder().url("http://192.168.191.1:8080/schoolinfor/huoqushuju?lianxi=" + loginName)
                        .get().build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {
                try {
                    String b = response.body().string();
                    JSONObject jsonObject = new JSONObject(b);
                    if (jsonObject.getString("result").equals("true")) {
                        Log.i("result", "返回了true");
                        userInforEntity.setMima(jsonObject.getString("mima"));
                        userInforEntity.setNicheng(jsonObject.getString("nicheng"));
                        userInforEntity.setXingming(jsonObject.getString("xingming"));
                        userInforEntity.setXibie(jsonObject.getString("xibie"));
                        userInforEntity.setBanji(jsonObject.getString("banji"));
                        userInforEntity.setXuehao(jsonObject.getString("xuehao"));
                        userInforEntity.setXingbie(jsonObject.getString("xingbie"));
                        userInforEntity.setShengri(jsonObject.getString("shengri"));
                        userInforEntity.setMinzu(jsonObject.getString("minzu"));
                        userInforEntity.setJia(jsonObject.getString("jia"));
                        userInforEntity.setXingqu(jsonObject.getString("xingqu"));
                        userInforEntity.setShenfen(jsonObject.getString("shenfen"));
                        userInforEntity.setTouxiang(jsonObject.getString("touxiang"));
                        userInforEntity.setZuoyouming(jsonObject.getString("zuoyouming"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return userInforEntity;
    }
}

