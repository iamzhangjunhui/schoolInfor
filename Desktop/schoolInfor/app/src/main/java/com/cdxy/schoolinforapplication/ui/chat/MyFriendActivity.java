package com.cdxy.schoolinforapplication.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWDBContact;
import com.alibaba.mobileim.contact.IYWOnlineContact;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.SchoolInforManager;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.adapter.MyFriendsAdapter;
import com.cdxy.schoolinforapplication.model.chat.MyFriendEntity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.ui.load.LoginActivity;
import com.cdxy.schoolinforapplication.ui.widget.NotifyDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.listview_my_friend)
    ListView listviewMyFriend;
    private List<MyFriendEntity> list;
    private MyFriendsAdapter adapter;
    private NotifyDialog notifyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        init();

        //添加好友
        IWxCallback addFriendCallback = new IWxCallback() {

            @Override
            public void onSuccess(Object... result) {
                toast("成功添加好友");
            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String info) {
                Log.i("iywContactService", "onError");
            }
        };
        LoginActivity.iywContactService.addContact("jiangguangguo", SchoolInforManager.appKay, "蒋光国", "你好，我是蒋光国", addFriendCallback);

        //获取是否在线
//        final IWxCallback iWxCallbackIsOnline = new IWxCallback() {
//            //已经在UI线程
//            @Override
//            public void onSuccess(Object... result) {
//                Map<String, IYWOnlineContact> contacts = (Map<String, IYWOnlineContact>) result[0];
//                if (contacts != null) {
//                    int i=0;
//                    for (Map.Entry<String, IYWOnlineContact> entry : contacts
//                            .entrySet()) {
//                        //用户userid
//                        String uid = entry.getKey();
//                        IYWOnlineContact ct = entry
//                                .getValue();
//                        //用户在线状态
//                        boolean online=ct.getOnlineStatus()==0?true:false;
//                        //...
//                        list.get(i).setOnline(online);
//                        i++;
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//            @Override
//            public void onError(int code,String info) {
//
//            }
//
//            @Override
//            public void onProgress(int progress) {
//
//            }
//        };
        IWxCallback callback = new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {

                List<IYWDBContact> contactsFromCache = LoginActivity.iywContactService.getContactsFromCache();
                //iywContactService.syncContactsOnlineStatus((List<IYWContact>) (List) contactsFromCache,iWxCallbackIsOnline);
                for (IYWDBContact iywdbContact : contactsFromCache) {
                    MyFriendEntity entity = new MyFriendEntity();
                    entity.setName(iywdbContact.getShowName());
                    entity.setIcon(iywdbContact.getAvatarPath());
                    entity.setUserId(iywdbContact.getUserId());
                    list.add(entity);
                }
                Log.i("iywContactService", list.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onProgress(int progress) {
            }


            @Override
            public void onError(int code, String info) {
                toast(info);
                Log.i("iywContactService", info);
            }
        };
        LoginActivity.iywContactService.syncContacts(callback);


    }

    @Override
    public void init() {
        txtTitle.setText("我的好友");
        list = new ArrayList<>();
        adapter = new MyFriendsAdapter(list, MyFriendActivity.this);
        listviewMyFriend.setAdapter(adapter);
        listviewMyFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String target = ((MyFriendEntity) adapter.getItem(i)).getUserId(); //消息接收者ID
                Intent intent = LoginActivity.ywimKit.getChattingActivityIntent(target, SchoolInforManager.appKay);
                startActivity(intent);
            }
        });
        listviewMyFriend.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                notifyDialog = new NotifyDialog(MyFriendActivity.this, R.style.MyDialog, new NotifyDialog.NotifyListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.dialog_ok:
                                deleteFriend(i);
                                notifyDialog.dismiss();
                                break;
                            case R.id.dialog_cancle:
                                notifyDialog.dismiss();
                                break;
                        }
                    }
                },MyFriendActivity.this);
                notifyDialog.show();


                return true;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;
        }
    }

    private void deleteFriend(final int position) {
        IWxCallback callback = new IWxCallback() {

            @Override
            public void onSuccess(Object... result) {
                if (result != null && result.length > 0) {
                    toast("删除好友成功");
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String info) {

            }
        };
        LoginActivity.iywContactService.delContact(((MyFriendEntity) adapter.getItem(position)).getUserId(), SchoolInforManager.appKay, callback);
    }
}
