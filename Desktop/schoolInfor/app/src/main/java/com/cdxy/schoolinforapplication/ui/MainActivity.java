package com.cdxy.schoolinforapplication.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.text1)
    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {

    }
}
