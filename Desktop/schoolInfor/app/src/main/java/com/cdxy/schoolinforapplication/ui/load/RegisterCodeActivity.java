package com.cdxy.schoolinforapplication.ui.load;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.util.Constant;
import com.cdxy.schoolinforapplication.util.NumberCheckUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterCodeActivity extends BaseActivity implements View.OnClickListener{

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
        switch (view.getId()){
            case R.id.btn_register_next:
                String registerName=edtRegisterName.getText().toString();
                String registerPassword=edtRegisterPassword.getText().toString();
                String registerSurePassword=edtRegisterSurePassword.getText().toString();
                if (TextUtils.isEmpty(registerName)){
                    toast("请输入手机号");
                    return;
                }
                if (!NumberCheckUtil.isMoibleNumber(registerName)){
                    toast("你输入的手机号格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(registerPassword)){
                    toast("请设置密码");
                    return;
                }
                if (registerPassword.length()<6){
                    toast("你设置的密码位数小于6位，请重新输入！");
                    return;
                }
                if (TextUtils.isEmpty(registerSurePassword)){
                    toast("请确认密码");
                    return;
                }
                if (!registerSurePassword.equals(registerPassword)){
                    toast("你两次输入的密码不一致,请重新确认");
                    edtRegisterSurePassword.setText("");
                    return;
                }
                Intent intent=new Intent(RegisterCodeActivity.this,RegisterActivity.class);
                intent.putExtra("registerName",registerName);
                intent.putExtra("registerPassword",registerPassword);
                intent.putExtra("registerSurePassword",registerSurePassword);
                startActivity(intent);
                break;
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;
            default:
                break;
        }
    }

}
