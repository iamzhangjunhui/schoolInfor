package com.cdxy.schoolinforapplication.ui.base;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.SchoolInforManager;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
   public abstract void  init();
  public void toast(String s){
      Toast.makeText(SchoolInforManager.getContext(),s,Toast.LENGTH_SHORT).show();
  }

}
