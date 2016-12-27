package com.cdxy.schoolinforapplication.ui.Message;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ui.base.BaseFragment;
import com.cdxy.schoolinforapplication.util.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.img_icon1)
    ImageView imgIcon1;
    @BindView(R.id.layout_important_message)
    LinearLayout layoutImportantMessage;
    @BindView(R.id.img_icon2)
    ImageView imgIcon2;
    @BindView(R.id.layout_not_important_message)
    LinearLayout layoutNotImportantMessage;
    @BindView(R.id.img_icon3)
    ImageView imgIcon3;
    @BindView(R.id.layout_my_message)
    LinearLayout layoutMyMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addListener();
    }

    @Override
    public void init() {
        //此处只是用于测试阶段获取用户身份
        new AlertDialog.Builder(getContext()).setMessage("我的身份是：").setPositiveButton("老师", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setNegativeButton("学生", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                layoutMyMessage.setVisibility(View.GONE);
                dialogInterface.dismiss();
            }
        }).create().show();
    }
    private  void addListener(){
        layoutImportantMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MessageListActivity.class);
                intent.putExtra("message_type", Constant.IMPORTANT_MESSAGE);
                startActivity(intent);
            }
        });
        layoutNotImportantMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MessageListActivity.class);
                intent.putExtra("message_type", Constant.NOT_IMPORTANT_MESSAGE);
                startActivity(intent);
            }
        });
        layoutMyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MessageListActivity.class);
                intent.putExtra("message_type", Constant.MY_SEND_MESSAGE);
                startActivity(intent);
            }
        });
    }
}
