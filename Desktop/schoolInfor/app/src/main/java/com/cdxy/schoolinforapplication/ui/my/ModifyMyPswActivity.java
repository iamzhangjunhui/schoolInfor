package com.cdxy.schoolinforapplication.ui.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.HttpUrl;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.model.UserInfor.UserInforEntity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.util.SharedPreferenceManager;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModifyMyPswActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_right)
    TextView txtRight;
    @BindView(R.id.edt_my_psw)
    EditText edtMyPsw;
    @BindView(R.id.edt_new_psw)
    EditText edtNewPsw;
    @BindView(R.id.edt_sure_new_psw)
    EditText edtSureNewPsw;
    @BindView(R.id.activity_modify_my_psw)
    LinearLayout activityModifyMyPsw;
    private String oldPassword;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_my_psw);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        init();
    }

    @Override
    public void init() {
        txtTitle.setText("修改密码");
        txtRight.setText("确认修改");
        txtRight.setVisibility(View.VISIBLE);
        UserInforEntity userInforEntity = SharedPreferenceManager.instance(ModifyMyPswActivity.this).getUserInfor();
        if (userInforEntity != null) {
            userid = userInforEntity.getUserid();
        }
        oldPassword=SharedPreferenceManager.instance(ModifyMyPswActivity.this).getMyPassword();
        edtMyPsw.setText(oldPassword + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;
            case R.id.txt_right:
                String newPassword = edtNewPsw.getText().toString();
                if (TextUtils.isEmpty(newPassword)) {
                    toast("请设置新密码");
                    return;
                }
                if ((!TextUtils.isEmpty(oldPassword)) && (!TextUtils.isEmpty(userid))) {
                    modifyPassword(userid, oldPassword, newPassword);
                } else {
                    toast("登录失效，请重新登录");
                }
                break;

        }
    }

    private void modifyPassword(String userid, String oldPassword, final String newPassword) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpUrl.UPDATE_PASSWORD + "?userid=" + userid + "&&oldPassword=" + oldPassword + "&&newPassword=" + oldPassword).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SharedPreferenceManager.instance(ModifyMyPswActivity.this).setMyPassword(newPassword);
                finish();
            }
        });
    }
}
