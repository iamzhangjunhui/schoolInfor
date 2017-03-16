package com.cdxy.schoolinforapplication.ui.chat;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListOperation;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.SchoolInforManager;
import com.cdxy.schoolinforapplication.ui.MainActivity;
import com.cdxy.schoolinforapplication.ui.load.LoginActivity;
import com.cdxy.schoolinforapplication.ui.widget.IsAddFriendDialog;

/**
 * Created by huihui on 2017/3/9.
 */

public class ConversationListOperationCustomSample extends IMConversationListOperation {
    private IsAddFriendDialog isAddFriendDialog;

    public ConversationListOperationCustomSample(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 定制会话点击事件，该方法可以定制所有的会话类型
     *
     * @param fragment     会话列表fragment
     * @param conversation 当前点击的会话对象
     * @return true: 使用用户自定义的点击事件  false：使用SDK默认的点击事件
                */
        @Override
        public boolean onItemClick(final Fragment fragment, final YWConversation conversation) {
            String s1=conversation.getLatestEServiceContactId();
        String s2=conversation.getConversationId();
        String s3=conversation.getLatestMessageAuthorId();
        YWConversationType type = conversation.getConversationType();
        if (type == YWConversationType.P2P) {
            Intent intent = LoginActivity.ywimKit.getChattingActivityIntent(conversation.getLatestEServiceContactId(), SchoolInforManager.appKay);
            fragment.getActivity().startActivity(intent);
            return true;
        } else if (type == YWConversationType.Tribe) {
            return true;
        } else if (type == YWConversationType.Custom) {
            isAddFriendDialog = new IsAddFriendDialog(fragment.getContext(), R.style.MyDialog, new IsAddFriendDialog.AddFriendListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_add:
                            addFriend(fragment,conversation.getLatestMessageAuthorId());
                            isAddFriendDialog.dismiss();
                            break;
                        case R.id.btn_not_add:
                            isAddFriendDialog.dismiss();
                            break;
                    }
                }
            },fragment.getActivity());
            isAddFriendDialog.show();

            return true;
        } else if (type == YWConversationType.unknow) {

            return true;
        } else if (type == YWConversationType.SHOP) {

            return true;
        } else if (type == YWConversationType.HJTribe) {

            return true;
        } else if (type == YWConversationType.CustomViewConversation) {
            return true;
        }


        return false;
    }

    private void addFriend(final Fragment fragment,String target) {
        IWxCallback callback = new IWxCallback() {

            @Override
            public void onSuccess(Object... result) {
               Toast.makeText(fragment.getContext(),"添加好友成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String info) {

            }
        };
        LoginActivity.iywContactService.ackAddContact(target, SchoolInforManager.appKay, true, isAddFriendDialog.respond, callback);

    }
}
