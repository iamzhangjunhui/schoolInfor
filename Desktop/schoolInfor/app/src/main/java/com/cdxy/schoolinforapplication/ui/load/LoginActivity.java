package com.cdxy.schoolinforapplication.ui.load;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.ui.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.txt_skip)
    TextView txtSkip;
    @BindView(R.id.edt_login_name)
    EditText edtLoginName;
    @BindView(R.id.edt_login_password)
    EditText edtLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_skip:
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register:
                intent = new Intent(LoginActivity.this, RegisterCodeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
