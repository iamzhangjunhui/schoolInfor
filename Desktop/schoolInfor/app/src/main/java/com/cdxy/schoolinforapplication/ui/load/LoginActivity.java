package com.cdxy.schoolinforapplication.ui.load;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.SchoolInforManager;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.model.UserInfor.UserInforEntity;
import com.cdxy.schoolinforapplication.ui.MainActivity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.util.SharedPreferenceManager;
import com.cdxy.schoolinforapplication.util.huoqushuju;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.edt_login_name)
    EditText edtLoginName;
    @BindView(R.id.edt_login_password)
    EditText edtLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    public static String userid;
    public static YWIMKit ywimKit;
    public static IYWContactService iywContactService;
    private String loginName;
    private huoqushuju huoqushuju;
    private UserInforEntity userInforEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        loginName = getIntent().getStringExtra("loginName");
        edtLoginName.setText(loginName);
    }

    @Override
    public void init() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                new AlertDialog.Builder(LoginActivity.this).setMessage("只有学生才需要注册，老师的账号学校已经配好，详情可以咨询教务处")
                        .setNegativeButton("我是老师", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setPositiveButton("我是学生", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LoginActivity.this, RegisterCodeActivity.class);
                        startActivity(intent);
                        dialogInterface.dismiss();
                    }
                }).create().show();

                break;
            case R.id.btn_login:
                loginName = edtLoginName.getText().toString();
                String loginPassword = edtLoginPassword.getText().toString();
//         (TextUtils.isEmpty(loginName)) {
//                    toast("请输入账号");
//                    return;
//                }
//                if (TextUtils.isEmpty(loginPassword)) {
//                    toast("请输入登录密码");
//                    return;
//                }
//                login(loginName, loginPassword);
                //获取SDK对象
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                getUserIdentity();
                userid ="zhangjunhui";
                ywimKit = YWAPI.getIMKitInstance(userid, SchoolInforManager.appKay);
                iywContactService = ywimKit.getContactService();
                //开始登录(测试使用，到时正式使用的时候需要放在登录我们的服务器成功之后)
                String password = "123456";
                IYWLoginService loginService = ywimKit.getLoginService();
                YWLoginParam param = new YWLoginParam(userid, password, SchoolInforManager.appKay);
                loginService.login(param, new IWxCallback() {
                    @Override
                    public void onSuccess(Object... objects) {

                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i) {

                    }
                });
            default:
                break;
        }
    }

    //苏杭    登陆接口
    public void login(final String a, final String b) {
        if (a.length() >= 6 && a.length() <= 16 && b.length() >= 6 && b.length() <= 16) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    OkHttpClient okHttpClient = new OkHttpClient();

                    Log.i("url", "http://192.168.191.1:8080/schoolinfor/login?lianxi=" + a + "&&mima=" + b);

                    Request request = new Request.Builder().url("http://192.168.191.1:8080/schoolinfor/login?lianxi=" + a + "&&mima=" + b)
                            .get().build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        String a = response.body().string();
                        JSONObject jsonObject = new JSONObject(a);
                        subscriber.onNext(jsonObject.getString("result"));
                        subscriber.onCompleted();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                //如果账号密码验证成功会返还true   这个时候需要跳转到主题界面
                @Override
                public void onNext(String s) {
                    if (s.equals("true")) {
                        SharedPreferences.Editor editor = SharedPreferenceManager.instance(LoginActivity.this).getEditor();
                        editor.putString(SharedPreferenceManager.LOGIN_NAME, a);//a为登录账号
                        editor.putString(SharedPreferenceManager.PASSWORD, b);//b为登录密码
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void getUserIdentity() {
        huoqushuju = new huoqushuju();
        userInforEntity = huoqushuju.huoqushuju(SharedPreferenceManager.instance(LoginActivity.this).getSharedPreferences().getString(SharedPreferenceManager.LOGIN_NAME, null));
        SharedPreferences.Editor editor = SharedPreferenceManager.instance(LoginActivity.this).getEditor();
        editor.putString(SharedPreferenceManager.IDENTITY, userInforEntity.getShenfen());
        editor.commit();
    }
}
