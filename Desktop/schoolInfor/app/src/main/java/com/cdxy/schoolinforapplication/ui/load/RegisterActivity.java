package com.cdxy.schoolinforapplication.ui.load;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.ui.ChooseInfor.ChooseInforActivity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.util.Constant;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @BindView(R.id.edt_nickname)
    EditText edtNickname;
    @BindView(R.id.activity_register)
    LinearLayout activityRegister;
    private String mRegisterName;
    private String mLoginPassword;
    private String mDepartment;
    private String mClazz;
    private String mNickName;
    private String mName;
    private String mStudentId;
    private String mSex;
    private String mBirthday;
    private String mNation;
    private String mAddress;
    private String mHobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        init();
        Intent intent=getIntent();
        mRegisterName=intent.getStringExtra("registerName");
        mLoginPassword=intent.getStringExtra("registerPassword");
    }

    @Override
    public void init() {
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
            case R.id.submit_register:
                mName=edtRealname.getText().toString();
                mNickName=edtNickname.getText().toString();
                mStudentId=edtStudentId.getText().toString();
                int checkSexId=rgSex.getCheckedRadioButtonId();
                if (checkSexId==R.id.girl){
                    mSex="女";
                }else {
                    mSex="男";
                }
                mBirthday=txtBirthday.getText().toString();
                mAddress=edtAddress.getText().toString();
                mHobby=edtHobby.getText().toString();
               if (TextUtils.isEmpty(mName)){
                   toast("请输入姓名");
               return;
               }
                if (TextUtils.isEmpty(mNickName)){
                    toast("请输入昵称");
                    return;
                }
                if (TextUtils.isEmpty(mDepartment)){
                    toast("请选择所在系");
                    return;
                }
                if (TextUtils.isEmpty(mClazz)){
                    toast("请选择所在班级");
                    return;
                }
                if (TextUtils.isEmpty(mStudentId)){
                    toast("请输入学号");
                    return;
                }
                register2(mRegisterName,mLoginPassword,mNickName,mName,mDepartment,mClazz,mStudentId,mSex,mBirthday,mNation,mAddress,mHobby);
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
                mDepartment=department;
            }
        }
        if (requestCode == Constant.REQUEST_CODE_CHOOSECLASS && resultCode == Constant.RESULT_CODE_CHOOSECLASS) {
            String clazz = data.getStringExtra("clazz");
            if (!TextUtils.isEmpty(clazz)) {
                txtClass.setText(clazz);
                mClazz=clazz;
            }
        }
        if (requestCode == Constant.REQUEST_CODE_CHOOSENATION && resultCode == Constant.RESULT_CODE_CHOOSENATION) {
            String nation = data.getStringExtra("nation");
            if (!TextUtils.isEmpty(nation)) {
                txtNation.setText(nation);
                mNation=nation;
            }
        }
    }
    //苏杭   注册接口第二布 register2   通过查找之前传输过来的账号密码匹对   然后把其他信息修改进数据库
    public void register2(final String a, final String b,final String c,final String d,final String e,final String f,final String g,
                          final String h,final String i,final String j,final String k,final String l){

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                OkHttpClient okHttpClient=new OkHttpClient();
                FormBody formBody=new FormBody.Builder().add("lianxi",a).add("mima",b).add("nicheng",c).add("xingming",d)
                        .add("xibie",e).add("banji",f).add("xuehao",g).add("xingbie",h).add("shengri",i).add("minzu",j)
                        .add("jia",k).add("xingqu",l).build();
                Request request=new Request.Builder().url("http://192.168.191.1:8080/schoolinfor/register2").post(formBody).build();
                try {
                    Response response=okHttpClient.newCall(request).execute();
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    Log.d("aaaaaaaaaa",jsonObject.getString("result"));
                    subscriber.onNext(jsonObject.getString("result"));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i("ssssss",s);
                if(s.equals("true")){
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("loginName", mRegisterName);
                    startActivity(intent);
                }
            }
        });

    }
}
