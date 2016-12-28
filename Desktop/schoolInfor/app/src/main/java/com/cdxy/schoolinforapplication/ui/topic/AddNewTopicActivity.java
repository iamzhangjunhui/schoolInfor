package com.cdxy.schoolinforapplication.ui.topic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @BindView(R.id.txt_new_topic)
    EditText txtNewTopic;
    @BindView(R.id.recycleView_add_photo)
    RecyclerView recycleViewAddPhoto;
    @BindView(R.id.layout_add_photo)
    LinearLayout layoutAddPhoto;
    @BindView(R.id.activity_add_new_topic)
    LinearLayout activityAddNewTopic;
    private List<Object> list;
    private ShowPhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_topic);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        init();
//        getPhotoPath();
    }

    @Override
    public void init() {
        txtTitle.setText("新建话题");
        btnRight.setText("发表");
        list = new ArrayList<>();
        //设置布局管理器，控制布局效果
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddNewTopicActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleViewAddPhoto.setLayoutManager(linearLayoutManager);
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
            case R.id.layout_add_photo:
                View dialogView = LayoutInflater.from(AddNewTopicActivity.this).inflate(R.layout.dialog_choose_way, null);
                TextView txtWay1 = (TextView) dialogView.findViewById(R.id.txt_way1);
                TextView txtWay2 = (TextView) dialogView.findViewById(R.id.txt_way2);
                txtWay1.setText("拍照");
                txtWay2.setText("从相册中获取");
                final AlertDialog dialog = new AlertDialog.Builder(AddNewTopicActivity.this).setView(dialogView).create();
                dialog.show();
                txtWay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //打开系统拍照程序，选择拍照图片
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, Constant.REQUEST_CODE_CAMERA);
                        dialog.dismiss();
                    }
                });
                txtWay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ////打开系统图库程序，选择图片
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, Constant.REQUEST_CODE_PICTURE);
                        dialog.dismiss();
                    }
                });
                break;
        }
    }
//测试数据
//    private void getPhotoPath() {
//        new AsyncTask<Void, Void, List<Object>>() {
//            @Override
//            protected List<Object> doInBackground(Void... voids) {
//                List<Object> objects = new ArrayList<Object>();
//                objects.add(R.drawable.bottom_tap_chat_false);
//                objects.add(R.drawable.bottom_tap_chat_false);
//                objects.add(R.drawable.bottom_tap_chat_false);
//                return objects;
//            }
//
//            @Override
//            protected void onPostExecute(List<Object> objects) {
//                super.onPostExecute(objects);
//                if (objects != null) {
//                    list.clear();
//                    list.addAll(objects);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        }.execute();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取拍照后返回的图片
        if (requestCode == Constant.REQUEST_CODE_CAMERA) {
            Bundle bundle = data.getExtras();
            //获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            list.add(bitmap);
            adapter.notifyItemInserted(list.size() - 1);

        }
        if (requestCode == Constant.REQUEST_CODE_PICTURE) {
            Uri selectedImage = data.getData();
            String[] filePathColumns={MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null,null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath= c.getString(columnIndex);
            list.add(picturePath);
            adapter.notifyItemInserted(list.size()-1);
            c.close();
        }
    }
}
