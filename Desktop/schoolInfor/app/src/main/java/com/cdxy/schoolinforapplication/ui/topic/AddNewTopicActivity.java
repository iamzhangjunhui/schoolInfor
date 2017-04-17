package com.cdxy.schoolinforapplication.ui.topic;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.Selection;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.adapter.Text1ListAdapter;
import com.cdxy.schoolinforapplication.adapter.topic.TopicPhotosAdapter;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.ui.widget.ChooseWayDialog;
import com.cdxy.schoolinforapplication.ui.widget.ScollerGridView;
import com.cdxy.schoolinforapplication.ui.widget.ScrollListView;
import com.cdxy.schoolinforapplication.util.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewTopicActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.activity_add_new_topic)
    LinearLayout activityAddNewTopic;
    @BindView(R.id.edt_new_topic)
    EditText edtNewTopic;
    @BindView(R.id.txt_remind_show_address)
    TextView txtRemindShowAddress;
    @BindView(R.id.layout_show_address)
    LinearLayout layoutShowAddress;
    @BindView(R.id.gridView_photos)
    ScollerGridView gridViewPhotos;
    @BindView(R.id.listview_address)
    ScrollListView listviewAddress;
    @BindView(R.id.img_indicator)
    ImageView imgIndicator;
    private List<Object> list;
    private TopicPhotosAdapter adapter;
    private String newTopic;
    private boolean isShowAddress;
    public LocationClient locationClient;
    public BDLocationListener myListener = new MyLocationListener();
    private Text1ListAdapter addressAdapter;
    private static List<String> addressList = new ArrayList<>();
    private ChooseWayDialog chooseWayDialog;

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
        initLocation();
        //注册监听函数
        locationClient.registerLocationListener(myListener);
        locationClient.start();
        listviewAddress.setOnItemClickListener(this);
    }

    @Override
    public void init() {
        txtTitle.setText("新建话题");
        btnRight.setText("发表");
        list = new ArrayList<>();
        list.add(R.drawable.remind_add_photo);
        adapter = new TopicPhotosAdapter(AddNewTopicActivity.this, list);
        gridViewPhotos.setAdapter(adapter);
        gridViewPhotos.setOnItemClickListener(this);
        gridViewPhotos.setOnItemLongClickListener(this);
        addressAdapter = new Text1ListAdapter(AddNewTopicActivity.this, addressList);
        listviewAddress.setAdapter(addressAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;
            case R.id.txt_remind_show_address:
                if (isShowAddress) {
                    isShowAddress = false;
                    listviewAddress.setVisibility(View.GONE);
                    imgIndicator.setImageResource(R.drawable.not_show);
                } else {
                    isShowAddress = true;
                    listviewAddress.setVisibility(View.VISIBLE);
                    imgIndicator.setImageResource(R.drawable.show);
                    if (addressList == null || addressList.size() == 0) {
                        toast("定位失败，没有获取到位置信息");
                    }
                }
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
                adapter.notifyDataSetChanged();
            }
            //从相册获取图片
            if (requestCode == Constant.REQUEST_CODE_PICTURE) {
/*                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String picturePath = c.getString(columnIndex);
                list.add(0, picturePath);
                adapter.notifyDataSetChanged();
                c.close();*/
                ArrayList<String> photos = data.getStringArrayListExtra("selectResult");
                for (int i = photos.size() - 1; i > -1; i--) {
                  Bitmap bitmap=  BitmapFactory.decodeFile(photos.get(i));
                    list.add(0, bitmap);
                }
                adapter.notifyDataSetChanged();

            }
        }
    }

    //LocationClientOption类，该类用来设置定位SDK的定位方式
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        int span = 60000;
        option.setCoorType("bd09ll");
        option.setIsNeedLocationDescribe(true);//比如 在。。。附近。
        option.setScanSpan(span);//设置一分钟定位一次
        option.setIsNeedAddress(true);//定位到的地址信息
        option.setOpenGps(true);
        option.setIsNeedLocationPoiList(true);//定位到的地址列表
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        locationClient.setLocOption(option);//之前一直无法获得地址描述，就是忘了写这一句代码。
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.gridView_photos:
                Object photo = adapter.getItem(i);
                if (photo instanceof Integer) {
                    if ((int) photo == R.drawable.remind_add_photo) {
                        chooseWayDialog = new ChooseWayDialog(AddNewTopicActivity.this, R.style.MyDialog, new ChooseWayDialog.ChooseWayDialogListener() {
                            @Override
                            public void onClick(View view) {
                                switch (view.getId()) {
                                    case R.id.txt_way1:
                                        //打开系统拍照程序，选择拍照图片
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (ActivityCompat.checkSelfPermission(AddNewTopicActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                            Toast.makeText(AddNewTopicActivity.this, "拍照的权限申请失败", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        AddNewTopicActivity.this.startActivityForResult(intent, Constant.REQUEST_CODE_CAMERA);
                                        chooseWayDialog.dismiss();
                                        break;
                                    case R.id.txt_way2:
                                        ////打开系统图库程序，选择图片
//                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        AddNewTopicActivity.this.startActivityForResult(intent, Constant.REQUEST_CODE_PICTURE);
                                        //自定义的获取图片的Activity
                                        intent = new Intent(AddNewTopicActivity.this, SelectPhotoActivity.class);
                                        startActivityForResult(intent, Constant.REQUEST_CODE_PICTURE);
                                        chooseWayDialog.dismiss();
                                        break;
                                }
                            }
                        }, AddNewTopicActivity.this, Constant.CHOOSE_WAY_DIALOG_TYPE_GET_PHOTO);
                        chooseWayDialog.show();
                    } else {
                        Intent intent = new Intent(AddNewTopicActivity.this, ShowBigPhotosActivity.class);
                        intent.putExtra("position", i);
                        intent.putExtra("photos", (Serializable) list);
                        intent.putExtra("photosType", Constant.SHOW_BIG_PHOTO_ADD_TOPIC);
                        AddNewTopicActivity.this.startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(AddNewTopicActivity.this, ShowBigPhotosActivity.class);
                    intent.putExtra("position", i);
                    intent.putExtra("photos", (Serializable) list);
                    intent.putExtra("photosType", Constant.SHOW_BIG_PHOTO_ADD_TOPIC);
                    startActivity(intent);
                }
                break;
            case R.id.listview_address:
                String addressInfor = addressList.get(i);
                newTopic = edtNewTopic.getText().toString();//创建话题编辑框的内容
                if (newTopic.contains("   --")) {//判断是否已经显示了地址信息
                    int j = newTopic.indexOf("   --");
                    if (newTopic.indexOf("   --") == 0) {//判断是否只显示了地址信息
                        newTopic = "";
                    } else {
                        newTopic = newTopic.split("   --")[0];//如果是，去除地址信息
                    }
                }
                if (!addressInfor.equals("不显示地址")) {
                    edtNewTopic.setText(newTopic + "   --" + addressInfor);
                } else {
                    edtNewTopic.setText(newTopic);
                }
                Selection.setSelection((Spannable) edtNewTopic.getText(), edtNewTopic.getText().toString().length());
                break;
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i < list.size() - 1) {
            ImageView imageView = (ImageView) view.findViewById(R.id.img_delete_photo);
            imageView.setVisibility(View.VISIBLE);
        }
        return true;
    }

    //BDLocationListener为结果监听接口
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            final List<Poi> addresses = location.getPoiList();
            if (addresses.size() > 0) {
                addressList.clear();
                addressList.add("不显示地址");
                for (int i = 0; i < addresses.size(); i++) {
                    addressList.add(addresses.get(i).getName());
                }
                addressAdapter.notifyDataSetChanged();
            }
        }
    }

}
