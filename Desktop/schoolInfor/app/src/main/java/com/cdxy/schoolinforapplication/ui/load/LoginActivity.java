package com.cdxy.schoolinforapplication.ui.load;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.utility.IMPrefsTools;
import com.cdxy.schoolinforapplication.HttpUrl;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.SchoolInforManager;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.model.LoginReturnEntity;
import com.cdxy.schoolinforapplication.model.ReturnEntity;
import com.cdxy.schoolinforapplication.model.UserInfor.UserInforEntity;
import com.cdxy.schoolinforapplication.ui.MainActivity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.ui.widget.ChooseIdentityTypeDialog;
import com.cdxy.schoolinforapplication.util.SharedPreferenceManager;
import com.cdxy.schoolinforapplication.util.huoqushuju;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
    public static YWIMKit ywimKit;
    public static IYWContactService iywContactService;
    private String loginName;
    private huoqushuju huoqushuju;
    private UserInforEntity userInforEntity;
    private ChooseIdentityTypeDialog chooseIdentityTypeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        //如果注册的时候返回说账号已经注册过了，返回登录界面是显示登录名
        loginName = getIntent().getStringExtra("loginName");
        edtLoginName.setText(loginName);
        //如果本地有登录信息就实现自动登录
        autoLogin();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                chooseIdentityTypeDialog = new ChooseIdentityTypeDialog(LoginActivity.this, R.style.MyDialog, new ChooseIdentityTypeDialog.ChooseIdentityTypeDialogListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_teacher:
                                chooseIdentityTypeDialog.dismiss();
                                break;
                            case R.id.btn_student:
                                Intent intent = new Intent(LoginActivity.this, RegisterCodeActivity.class);
                                startActivity(intent);
                                chooseIdentityTypeDialog.dismiss();
                                break;
                        }
                    }
                }, LoginActivity.this);
                chooseIdentityTypeDialog.show();
                break;
            case R.id.btn_login:
                loginName = edtLoginName.getText().toString();
                String loginPassword = edtLoginPassword.getText().toString();
                if (TextUtils.isEmpty(loginName)) {
                    toast("请输入账号");
                    return;
                }
                if (TextUtils.isEmpty(loginPassword)) {
                    toast("请输入登录密码");
                    return;
                }
                login(loginName, loginPassword);
//                getUserIdentity();
            default:
                break;
        }
    }

    //苏杭    登陆接口
    public void login(final String userid, final String password) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpUrl.LOGIN + "?userid=" + userid + "&&password=" + password).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Observable.just(result).map(new Func1<String, ReturnEntity<LoginReturnEntity>>() {
                    @Override
                    public ReturnEntity<LoginReturnEntity> call(String s) {
                        Gson gson = new Gson();
                        ReturnEntity<LoginReturnEntity> returnEntity = gson.fromJson(s, ReturnEntity.class);
                        if (returnEntity != null) {
                            returnEntity = gson.fromJson(s, new TypeToken<ReturnEntity<LoginReturnEntity>>() {
                            }.getType());
                        }
                        return returnEntity;
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ReturnEntity<LoginReturnEntity>>() {
                            @Override
                            public void call(ReturnEntity<LoginReturnEntity> loginReturnEntityReturnEntity) {
                                //如果登陆成功
                                if (loginReturnEntityReturnEntity.getCode() == 1) {
                                    LoginReturnEntity loginReturnEntity = loginReturnEntityReturnEntity.getData();
                                    aliLogin(loginReturnEntity.getUserid(), loginReturnEntity.getPassword());
                                } else {
                                    toast("登录出现异常");
                                }
                            }
                        });
            }

        });
    }

    private void getUserIdentity() {
        huoqushuju = new huoqushuju();
        userInforEntity = huoqushuju.huoqushuju(SharedPreferenceManager.instance(LoginActivity.this).getSharedPreferences().getString(SharedPreferenceManager.LOGIN_NAME, null));
        SharedPreferences.Editor editor = SharedPreferenceManager.instance(LoginActivity.this).getEditor();
        editor.putString(SharedPreferenceManager.IDENTITY, userInforEntity.getShenfen());
        editor.commit();
    }

    private void aliLogin(final String userid, final String password) {
        ywimKit = YWAPI.getIMKitInstance(userid, SchoolInforManager.appKay);
        iywContactService = ywimKit.getContactService();
        //开始登录(测试使用，到时正式使用的时候需要放在登录我们的服务器成功之后)
        IYWLoginService loginService = ywimKit.getLoginService();
        YWLoginParam param = new YWLoginParam(userid, password, SchoolInforManager.appKay);
        loginService.login(param, new IWxCallback() {
            @Override
            public void onSuccess(Object... objects) {
                saveLoginInforToLocal(userid, password);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i) {

            }
        });
    }

    private void saveLoginInforToLocal(String userid, String password) {
        IMPrefsTools.setStringPrefs(LoginActivity.this, "USER_ID", userid);
        IMPrefsTools.setStringPrefs(LoginActivity.this, "PASSWORD", password);
    }

    private void autoLogin() {
        String userid = IMPrefsTools.getStringPrefs(LoginActivity.this, "USER_ID");
        String password = IMPrefsTools.getStringPrefs(LoginActivity.this, "PASSWORD");
        if (!TextUtils.isEmpty(userid) && (!TextUtils.isEmpty(password))) {
            ywimKit = YWAPI.getIMKitInstance(userid, SchoolInforManager.appKay);
            iywContactService = ywimKit.getContactService();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}