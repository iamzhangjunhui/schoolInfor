package com.cdxy.schoolinforapplication.ui.topic;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.adapter.ShowPhotoAdapter;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.util.Constant;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewTopicActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.recycleView_add_photo)
    RecyclerView recycleViewAddPhoto;
    @BindView(R.id.activity_add_new_topic)
    LinearLayout activityAddNewTopic;
    @BindView(R.id.edt_new_topic)
    EditText edtNewTopic;
    @BindView(R.id.txt_remind_show_address)
    TextView txtRemindShowAddress;
    @BindView(R.id.txt_now_address)
    TextView txtNowAddress;
    @BindView(R.id.layout_show_address)
    LinearLayout layoutShowAddress;
    private List<Object> list;
    private ShowPhotoAdapter adapter;
    private String newTopic;
    private boolean isShowAddress = false;
    public LocationClient locationClient;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_topic);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        init();
        //注意：LocationClient类必须在主线程中声明
        //Context需要时全进程有效的Context,推荐用getApplicationConext获取全进程有效的Context。
        // //声明LocationClient类
        locationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        locationClient.registerLocationListener(myListener);
        initLocation();
        locationClient.start();
    }

    @Override
    public void init() {
        txtTitle.setText("新建话题");
        btnRight.setText("发表");
        list = new ArrayList<>();
        list.add(R.drawable.remind_add_photo);
        //设置布局管理器，控制布局效果
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AddNewTopicActivity.this, 5);
        recycleViewAddPhoto.setLayoutManager(gridLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recycleViewAddPhoto.setHasFixedSize(true);
        adapter = new ShowPhotoAdapter(AddNewTopicActivity.this, list);
        recycleViewAddPhoto.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //获取拍照后返回的图片
            if (requestCode == Constant.REQUEST_CODE_CAMERA) {
                Bundle bundle = data.getExtras();
                //获取相机返回的数据，并转换为图片格式
                Bitmap bitmap = (Bitmap) bundle.get("data");
                list.add(0, bitmap);
                adapter.notifyItemInserted(0);

            }
            //从相册获取图片
            if (requestCode == Constant.REQUEST_CODE_PICTURE) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String picturePath = c.getString(columnIndex);
                list.add(0, picturePath);
                adapter.notifyItemInserted(0);
                c.close();
            }
        }
    }

    //LocationClientOption类，该类用来设置定位SDK的定位方式
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        int span = 60000;
        option.setCoorType("bd09ll");
        option.setIsNeedLocationDescribe(true);
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
    }

    //BDLocationListener为结果监听接口
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("\nlocationdescribe : ");
            final String address = location.getLocationDescribe();
            sb.append(address);// 位置语义化信息
            if (!TextUtils.isEmpty(address)) {
                layoutShowAddress.setVisibility(View.VISIBLE);
                txtNowAddress.setText(address);
                txtRemindShowAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isShowAddress == false) {
                            isShowAddress = true;
                            newTopic = edtNewTopic.getText().toString();//创建话题编辑框的内容
                            txtRemindShowAddress.setText("取消显示当前地址");
                            edtNewTopic.setText(newTopic + "   --" + address);
                        } else {
                            isShowAddress = false;
                            newTopic = edtNewTopic.getText().toString();
                            txtRemindShowAddress.setText("显示当前地址");
                            String newTopicContent = newTopic.substring(0, newTopic.lastIndexOf("--") - 1);
                            edtNewTopic.setText(newTopicContent + "");
                        }
                    }
                });
            }


        }
    }

}
