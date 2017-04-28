package com.cdxy.schoolinforapplication.ui.load;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.HttpUrl;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.model.ReturnEntity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.util.Constant;
import com.cdxy.schoolinforapplication.util.NumberCheckUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RegisterCodeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.edt_register_name)
    EditText edtRegisterName;
    @BindView(R.id.edt_register_password)
    EditText edtRegisterPassword;
    @BindView(R.id.edt_register_sure_password)
    EditText edtRegisterSurePassword;
    @BindView(R.id.btn_register_next)
    Button btnRegisterNext;
    private String registerName;
    private String registerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        init();
    }

    @Override
    public void init() {
        txtTitle.setText("注册");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register_next:
                registerName = edtRegisterName.getText().toString();
                registerPassword = edtRegisterPassword.getText().toString();
                String registerSurePassword = edtRegisterSurePassword.getText().toString();
                if (TextUtils.isEmpty(registerPassword)) {
                    toast("请设置密码");
                    return;
                }
                if (registerPassword.length() < 6) {
                    toast("你设置的密码位数小于6位，请重新输入！");
                    return;
                }
                if (TextUtils.isEmpty(registerSurePassword)) {
                    toast("请确认密码");
                    return;
                }
                if (!registerSurePassword.equals(registerPassword)) {
                    toast("你两次输入的密码不一致,请重新确认");
                    edtRegisterSurePassword.setText("");
                    return;
                }
                register1();
                break;
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;
            default:
                break;
        }
    }

    //苏杭    注册接口第一步 register1  此处只向数据库加入账号和密码  如果返回了false说明账号被注册了需要重新注册下面的onNext中
    public void register1() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpUrl.REGISTER + "?userid=" + registerName + "&&password" + registerPassword).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Observable.just(response.body().string()).map(new Func1<String, ReturnEntity>() {
                    @Override
                    public ReturnEntity call(String s) {
                        Gson gson = new Gson();
                        ReturnEntity returnEntity = gson.fromJson(s, ReturnEntity.class);
                        return returnEntity;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ReturnEntity>() {
                            @Override
                            public void call(ReturnEntity returnEntity) {
                                if (returnEntity != null) {
                                    if (returnEntity.getCode() == 1) {
                                        toast("注册成功");
                                        Intent intent = new Intent(RegisterCodeActivity.this, RegisterActivity.class);
                                        intent.putExtra("registerName", registerName);
                                        intent.putExtra("registerPassword", registerPassword);
                                        startActivity(intent);
                                    }else {
                                        toast(returnEntity.getMsg()+"");
                                    }
//                                        else if () {
                                    //该账号已经注册过了
//                                            new AlertDialog.Builder(RegisterCodeActivity.this).setMessage("该手机号已经注册过，请直接登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    Intent intent = new Intent(RegisterCodeActivity.this, LoginActivity.class);
//                                                    intent.putExtra("loginName", registerName);
//                                                    startActivity(intent);
//                                                    dialogInterface.dismiss();
//                                                }
//                                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    dialogInterface.dismiss();
//                                                }
//                                            }).create().show();
//                                        }
                                } else {
                                    toast("注册失败");
                                }
                            }
                        });
            }
        });

    }
}
