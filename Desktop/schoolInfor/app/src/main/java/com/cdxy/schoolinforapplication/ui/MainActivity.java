package com.cdxy.schoolinforapplication.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.ui.load.LoginActivity;
import com.cdxy.schoolinforapplication.ui.my.ModifyMyInforActivity;
import com.cdxy.schoolinforapplication.ui.my.ModifyMyPswActivity;
import com.cdxy.schoolinforapplication.ui.my.MyInformationActivity;
import com.cdxy.schoolinforapplication.ui.widget.DragLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.img_my_icon)
    ImageView imgMyIcon;
    @BindView(R.id.txt_my_name)
    TextView txtMyName;
    @BindView(R.id.txt_my_department)
    TextView txtMyDepartment;
    @BindView(R.id.txt_my_clazz)
    TextView txtMyClazz;
    @BindView(R.id.txt_my_motto)
    TextView txtMyMotto;
    @BindView(R.id.txt_my_information)
    TextView txtMyInformation;
    @BindView(R.id.txt_my_modify_psw)
    TextView txtMyModifyPsw;
    @BindView(R.id.txt_exit)
    TextView txtExit;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.draglayout)
    DragLayout draglayout;
    private long exitTime = 0;
    private boolean isOpon;//侧滑栏是否打开

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        Glide.with(MainActivity.this).load(R.drawable.students).bitmapTransform(new CropCircleTransformation(MainActivity.this)).into(imgMyIcon);
        Glide.with(MainActivity.this).load(R.drawable.students).bitmapTransform(new CropCircleTransformation(MainActivity.this)).into(imgIcon);
    }

    @Override
    public void init() {

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
                //点击顶部导航栏的头像后，判断侧滑栏是否打开，再执行打开或关闭操作
                if (isOpon) {
                    draglayout.close();
                    isOpon = false;

                } else {
                    draglayout.open();
                    isOpon = true;
                }
                break;
            case R.id.txt_my_motto:
                //修改我的座右铭
                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_my_modify_psd, null);
                EditText edtModifyMyMotto = (EditText) dialogView.findViewById(R.id.edt_modify_my_motto);
                Button btnSaveModifyMotto = (Button) dialogView.findViewById(R.id.btn_save_modify_motto);
                Button btnCancelMyModifyMotto = (Button) dialogView.findViewById(R.id.btn_cancel_my_modify_motto);
                final Dialog dialog = new AlertDialog.Builder(MainActivity.this).setView(dialogView).create();
                dialog.show();
                final String modifyMotto = edtModifyMyMotto.getText().toString();
                btnSaveModifyMotto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //此处将修改后的motto上传至服务器。

                        dialog.dismiss();
                    }
                });
                btnCancelMyModifyMotto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.txt_my_information:
                //进入我的信息详情界面
                Intent intent = new Intent(MainActivity.this, MyInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_my_modify_psw:
                //进入修改密码界面
                intent = new Intent(MainActivity.this, ModifyMyPswActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_exit:
                ScreenManager.getScreenManager().appExit(this);
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

        }
    }
}
