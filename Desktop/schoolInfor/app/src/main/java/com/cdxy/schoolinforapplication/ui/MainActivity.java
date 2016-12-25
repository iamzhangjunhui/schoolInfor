package com.cdxy.schoolinforapplication.ui;

import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private long exitTime = 0;
    @BindView(R.id.text1)
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        initView();
    }

    @Override
    public void initView() {

    }

    //判断双击时间间隔是否小于2秒
    public void exitAppBy2Click() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            exitTime = System.currentTimeMillis();
        } else {
            ScreenManager.getScreenManager().appExit(MainActivity.this);
        }
    }
//双击退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitAppBy2Click();
        }
        return true;
    }
}
