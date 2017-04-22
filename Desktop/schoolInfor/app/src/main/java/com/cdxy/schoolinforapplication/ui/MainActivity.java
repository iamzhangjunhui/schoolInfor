package com.cdxy.schoolinforapplication.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.ui.Message.SendMessageActivity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.ui.chat.MyFriendActivity;
import com.cdxy.schoolinforapplication.ui.load.LoginActivity;
import com.cdxy.schoolinforapplication.ui.my.ModifyMyPswActivity;
import com.cdxy.schoolinforapplication.ui.my.MyInformationActivity;
import com.cdxy.schoolinforapplication.ui.topic.AddNewTopicActivity;
import com.cdxy.schoolinforapplication.ui.widget.DragLayout;
import com.cdxy.schoolinforapplication.ui.widget.ModifyMyMottoDialog;
import com.cdxy.schoolinforapplication.util.SharedPreferenceManager;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
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
    @BindView(R.id.frame_container)
    FrameLayout frameContainer;
    @BindView(R.id.img_bottom_chat)
    ImageView imgBottomChat;
    @BindView(R.id.txt_bottom_chat)
    TextView txtBottomChat;
    @BindView(R.id.layout_bottom_chat)
    LinearLayout layoutBottomChat;
    @BindView(R.id.img_bottom_topic)
    ImageView imgBottomTopic;
    @BindView(R.id.txt_bottom_topic)
    TextView txtBottomTopic;
    @BindView(R.id.layout_bottom_topic)
    LinearLayout layoutBottomTopic;
    @BindView(R.id.img_bottom_message)
    ImageView imgBottomMessage;
    @BindView(R.id.txt_bottom_message)
    TextView txtBottomMessage;
    @BindView(R.id.layout_bottom_message)
    LinearLayout layoutBottomMessage;
    @BindView(R.id.draglayout)
    DragLayout draglayout;
    private long exitTime = 0;
    private boolean isOpon;//侧滑栏是否打开
    private FragmentManager fragmentManager;
    private TextView[] textViews;
    private ImageView[] imageViews;
    private Fragment[] fragments;
    private int oldPos = 1;
    private ModifyMyMottoDialog modifyMyMottoDialog;
    public static String TAG = "SchoolInforManager";
    private static final int MSG_SET_TAGS = 1001;
    private Handler mHandler;
    Set<String> tags = new HashSet<>();
    private TagAliasCallback mAliasCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        init();
        txtTitle.setText("话题");
        btnRight.setText("创建话题");
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewTopicActivity.class);
                startActivity(intent);
            }
        });
        setFragments(1);
    }

    @Override
    public void init() {
        Glide.with(MainActivity.this).load(R.drawable.students).placeholder(R.drawable.loading).bitmapTransform(new CropCircleTransformation(MainActivity.this)).into(imgMyIcon);
        Glide.with(MainActivity.this).load(R.drawable.students).placeholder(R.drawable.loading).bitmapTransform(new CropCircleTransformation(MainActivity.this)).into(imgIcon);
        if (fragmentManager == null) {

            fragmentManager = getSupportFragmentManager();
        }
        fragments = new Fragment[]{fragmentManager.findFragmentById(R.id.chat_fragment),
                fragmentManager.findFragmentById(R.id.topic_fragment),
                fragmentManager.findFragmentById(R.id.message_fragment)};
        textViews = new TextView[]{txtBottomChat, txtBottomTopic, txtBottomMessage};
        imageViews = new ImageView[]{imgBottomChat, imgBottomTopic, imgBottomMessage};

    }

    //判断双击时间间隔是否小于2秒
    public void exitAppBy2Click() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            exitTime = System.currentTimeMillis();
        } else {
            ScreenManager.getScreenManager().appExit(MainActivity.this);
        }
    }

    private void setFragments(int selectPos) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (selectPos < oldPos) {
            //该方法实现的是fragment跳转时的动画效果
            transaction.setCustomAnimations(R.anim.fragment_in_1, R.anim.fragment_out_1);
        } else if (selectPos > oldPos) {
            transaction.setCustomAnimations(R.anim.fragment_in_2, R.anim.fragment_out_2);
        }
        transaction.commit();
        oldPos = selectPos;
        for (int i = 0; i < textViews.length; i++) {
            if (i == selectPos) {
                transaction.show(fragments[i]);
                textViews[i].setTextColor(getResources().getColor(R.color.text_bottom_tap_color));
                setImage(i, true);
            } else {
                transaction.hide(fragments[i]);
                textViews[i].setTextColor(getResources().getColor(R.color.white));
                setImage(i, false);
            }
        }
        JPushAddTags();
    }

    //极光推送添加Tags
    private void JPushAddTags() {
        //极光推送设置Tag的回调
        mAliasCallback = new TagAliasCallback() {
            @Override
            public void gotResult(int code, String alias, Set<String> tags) {
                String logs;
                switch (code) {
                    case 0:
                        logs = "Set tag success";
                        SharedPreferences.Editor editor = SharedPreferenceManager.instance(MainActivity.this).getEditor();
                        editor.putBoolean(SharedPreferenceManager.ISADDTAG, true);
                        editor.commit();
                        Log.i(TAG, logs);
                        // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                        break;
                    case 6002:
                        logs = "Failed to tags due to timeout. Try again after 60s.";
                        Log.i(TAG, logs);
                        // 延迟 60 秒来调用 Handler 设置别名
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, alias), 1000 * 60);
                        break;
                    default:
                        logs = "Failed with errorCode = " + code;
                        Log.e(TAG, logs);
                }
                Toast.makeText(MainActivity.this, logs, Toast.LENGTH_SHORT).show();
            }
        };
        mHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_SET_TAGS:
                        Log.d(TAG, "Set alias in handler.");
                        // 调用 JPush 接口来设置标签。
                        JPushInterface.setTags(MainActivity.this, tags, mAliasCallback);
                        break;
                    default:
                        Log.i(TAG, "Unhandled msg - " + msg.what);
                }
            }
        };
        tags.add("计算机系");
        tags.add("嵌入式");
        SharedPreferences sharedPreferences = SharedPreferenceManager.instance(MainActivity.this).getSharedPreferences();
        boolean isAddTags = sharedPreferences.getBoolean(SharedPreferenceManager.ISADDTAG, false);
        if (!isAddTags) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tags));
        }
    }

    private void setImage(int selectPos, boolean siselect) {
        switch (selectPos) {
            case 0:
                imageViews[0].setImageDrawable(
                        siselect ? getResources().getDrawable(R.drawable.bottom_tap_chat_true) :
                                getResources().getDrawable(R.drawable.bottom_tap_chat_false));
                break;
            case 1:
                imageViews[1].setImageDrawable(
                        siselect ? getResources().getDrawable(R.drawable.bottom_tap_topic_true) :
                                getResources().getDrawable(R.drawable.bottom_tap_topic_false));
                break;
            case 2:
                imageViews[2].setImageDrawable(
                        siselect ? getResources().getDrawable(R.drawable.bottom_tap_message_true) :
                                getResources().getDrawable(R.drawable.bottom_tap_message_false));
                break;
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
                modifyMyMottoDialog = new ModifyMyMottoDialog(MainActivity.this, R.style.MyDialog, new ModifyMyMottoDialog.ModifyMottoDialogListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.btn_save_modify_motto:
                                //此处将修改后的motto上传至服务器。
                                toast(modifyMyMottoDialog.newMotto);
                                modifyMyMottoDialog.dismiss();
                                break;
                            case R.id.btn_cancel_my_modify_motto:
                                modifyMyMottoDialog.dismiss();
                                break;

                        }
                    }
                }, MainActivity.this);
                modifyMyMottoDialog.show();
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
            case R.id.layout_bottom_chat:
                setFragments(0);
                txtTitle.setText("会话中心");
                btnRight.setText("我的好友");
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, MyFriendActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.layout_bottom_topic:
                txtTitle.setText("话题");
                btnRight.setText("创建话题");
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddNewTopicActivity.class);
                        startActivity(intent);
                    }
                });
                setFragments(1);
                break;
            case R.id.layout_bottom_message:
                txtTitle.setText("消息中心");
//                String identity=SharedPreferenceManager.instance(MainActivity.this).getSharedPreferences().getString(SharedPreferenceManager.IDENTITY,null);
                String identity = "老师";
                if (identity.equals("老师")) {
                    btnRight.setText("发送消息");
                    btnRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, SendMessageActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    btnRight.setText("");
                }
                setFragments(2);
                break;

        }
    }
}
