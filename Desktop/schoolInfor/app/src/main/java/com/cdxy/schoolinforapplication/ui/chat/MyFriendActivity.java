package com.cdxy.schoolinforapplication.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWDBContact;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyFriendActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.activity_my_friend)
    LinearLayout activityMyFriend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        init();
        //获取我的好友列表
        YWIMKit ywimKit = YWAPI.getIMKitInstance("visitor1", "23015524");
        final IYWContactService iywContactService = ywimKit.getContactService();
        IWxCallback callback = new IWxCallback() {


            @Override
            public void onSuccess(Object... result) {
                List<IYWDBContact> contactsFromCache = iywContactService.getContactsFromCache();
            }

            @Override
            public void onProgress(int progress) {

            }


            @Override
            public void onError(int code, String info) {

            }
        };
        iywContactService.syncContacts(callback);
    }

    @Override
    public void init() {
        txtTitle.setText("我的好友");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;
        }
    }
}
