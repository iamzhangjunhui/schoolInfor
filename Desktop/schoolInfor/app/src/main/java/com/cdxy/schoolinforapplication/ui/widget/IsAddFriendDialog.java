package com.cdxy.schoolinforapplication.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.cdxy.schoolinforapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huihui on 2017/3/15.
 */

public class IsAddFriendDialog extends Dialog implements View.OnClickListener {
    @BindView(R.id.edt_respond)
    EditText edtRespond;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.btn_not_add)
    Button btnNotAdd;
    private AddFriendListener addFriendListener;
    public String respond = "";
    private Activity activity;

    public IsAddFriendDialog(@NonNull Context context, @StyleRes int themeResId, AddFriendListener addFriendListener,Activity activity) {
        super(context, themeResId);
        this.addFriendListener = addFriendListener;
        this.activity=activity;
    }

    @Override
    public void onClick(View view) {
        respond = edtRespond.getText().toString();
        addFriendListener.onClick(view);
    }

    public interface AddFriendListener {
        void onClick(View view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_friend);
        ButterKnife.bind(this);
        //设置dialog显示的宽度
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.width=activity.getWindowManager().getDefaultDisplay().getWidth()-40;
        getWindow().setAttributes(params);
        btnAdd.setOnClickListener(this);
        btnNotAdd.setOnClickListener(this);
    }
}
