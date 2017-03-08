package com.cdxy.schoolinforapplication.ui.chat;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWDBContact;
import com.bumptech.glide.Glide;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ui.MainActivity;
import com.cdxy.schoolinforapplication.ui.base.BaseFragment;
import com.cdxy.schoolinforapplication.ui.load.LoginActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment {
    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        Fragment f = LoginActivity.ywimKit.getConversationFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.chat_conversation_fragment,f).commit();
    }

    @Override
    public void init() {

    }

}
