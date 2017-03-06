package com.cdxy.schoolinforapplication.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.model.UserInfor.UserInforEntity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.util.SharedPreferenceManager;
import com.cdxy.schoolinforapplication.util.huoqushuju;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyInformationActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_right)
    TextView txtRight;
    @BindView(R.id.txt_nickname)
    TextView txtNickname;
    @BindView(R.id.txt_realname)
    TextView txtRealname;
    @BindView(R.id.txt_department)
    TextView txtDepartment;
    @BindView(R.id.txt_class)
    TextView txtClass;
    @BindView(R.id.txt_student_id)
    TextView txtStudentId;
    @BindView(R.id.txt_sex)
    TextView txtSex;
    @BindView(R.id.txt_birthday)
    TextView txtBirthday;
    @BindView(R.id.txt_nation)
    TextView txtNation;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_hobby)
    TextView txtHobby;
    @BindView(R.id.activity_my_information)
    LinearLayout activityMyInformation;
    private huoqushuju huoqushuju;
    private UserInforEntity userInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        ScreenManager.getScreenManager().pushActivity(this);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        txtTitle.setText("我的个人信息");
        txtRight.setVisibility(View.VISIBLE);
        huoqushuju = new huoqushuju();
        setData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;
            case R.id.txt_right:
                Intent intent = new Intent(MyInformationActivity.this, ModifyMyInforActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void setData() {
        String loginName = SharedPreferenceManager.instance(MyInformationActivity.this).getSharedPreferences().getString(SharedPreferenceManager.LOGIN_NAME, null);
        if (!TextUtils.isEmpty(loginName)) {
            userInfor = huoqushuju.huoqushuju(loginName);
            txtNickname.setText(userInfor.getNicheng() + "");
            txtRealname.setText(userInfor.getXingming() + "");
            txtDepartment.setText(userInfor.getXibie() + "");
            txtClass.setText(userInfor.getBanji() + "");
            txtStudentId.setText(userInfor.getXuehao() + "");
            txtSex.setText(userInfor.getXingbie() + "");
            txtBirthday.setText(userInfor.getShengri() + "");
            txtNation.setText(userInfor.getMinzu() + "");
            txtAddress.setText(userInfor.getJia() + "");
            txtHobby.setText(userInfor.getXingqu() + "");
        }

    }

}
