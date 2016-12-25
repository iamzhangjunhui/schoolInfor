package com.cdxy.schoolinforapplication.ui.load;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.ui.ChooseInfor.ChooseInforActivity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.util.Constant;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.edt_realname)
    EditText edtRealname;
    @BindView(R.id.txt_department)
    TextView txtDepartment;
    @BindView(R.id.txt_class)
    TextView txtClass;
    @BindView(R.id.edt_student_id)
    EditText edtStudentId;
    @BindView(R.id.girl)
    RadioButton girl;
    @BindView(R.id.boy)
    RadioButton boy;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.txt_birthday)
    TextView txtBirthday;
    @BindView(R.id.txt_nation)
    TextView txtNation;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.edt_hobby)
    EditText edtHobby;
    @BindView(R.id.submit_register)
    Button submitRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        initView();
    }

    @Override
    public void initView() {
        txtTitle.setText("注册");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;
            case R.id.txt_department:
                Intent intent = new Intent(RegisterActivity.this, ChooseInforActivity.class);
                intent.putExtra("flagSelectInforFrom", Constant.FLAG_REQURST_FROM_DEPARTMENT);
                startActivityForResult(intent, Constant.REQUEST_CODE_CHOOSEDEPARTMENT);
                break;
            case R.id.txt_class:
                intent = new Intent(RegisterActivity.this, ChooseInforActivity.class);
                intent.putExtra("flagSelectInforFrom", Constant.FLAG_REQURST_FROM_CLASS);
                String department = txtDepartment.getText().toString();
                if (TextUtils.isEmpty(department)) {
                    toast("请先选择你所在系");
                    return;
                } else {
                    intent.putExtra("department", department);
                    startActivityForResult(intent, Constant.REQUEST_CODE_CHOOSECLASS);
                }
                break;
            case R.id.txt_nation:
                intent = new Intent(RegisterActivity.this, ChooseInforActivity.class);
                intent.putExtra("flagSelectInforFrom", Constant.FLAG_REQURST_FROM_NATION);
                startActivityForResult(intent, Constant.REQUEST_CODE_CHOOSENATION);
                break;
            case R.id.txt_birthday:
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txtBirthday.setText(i + "年" + (i1 + 1) + "月" + i2 + "日");
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CHOOSEDEPARTMENT && resultCode == Constant.RESULT_CODE_CHOOSEDEPARTMENT) {
            String department = data.getStringExtra("department");
            if (!TextUtils.isEmpty(department)) {
                txtDepartment.setText(department);
            }
        }
        if (requestCode == Constant.REQUEST_CODE_CHOOSECLASS && resultCode == Constant.RESULT_CODE_CHOOSECLASS) {
            String clazz = data.getStringExtra("clazz");
            if (!TextUtils.isEmpty(clazz)) {
                txtClass.setText(clazz);
            }
        }
        if (requestCode == Constant.REQUEST_CODE_CHOOSENATION && resultCode == Constant.RESULT_CODE_CHOOSENATION) {
            String nation = data.getStringExtra("nation");
            if (!TextUtils.isEmpty(nation)) {
                txtNation.setText(nation);
            }
        }
    }
}
