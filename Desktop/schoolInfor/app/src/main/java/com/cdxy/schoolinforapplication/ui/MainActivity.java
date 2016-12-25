package com.cdxy.schoolinforapplication.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.ui.widget.DragLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    DragLayout draglayout;
    @BindView(R.id.my_icon)
    ImageView myIcon;
    @BindView(R.id.my_name)
    TextView myName;
    @BindView(R.id.my_department)
    TextView myDepartment;
    @BindView(R.id.my_clazz)
    TextView myClazz;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    private long exitTime = 0;
    private boolean isOpon;//侧滑栏是否打开

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        initView();
        Glide.with(MainActivity.this).load(R.drawable.students).bitmapTransform(new CropCircleTransformation(MainActivity.this)).into(myIcon);
        Glide.with(MainActivity.this).load(R.drawable.students).bitmapTransform(new CropCircleTransformation(MainActivity.this)).into(imgIcon);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_icon:
                if (isOpon) {
                    draglayout.close();
                    isOpon = false;

                } else {
                    draglayout.open();
                    isOpon = true;
                }
        }
    }
}
