package com.cdxy.schoolinforapplication.ui.Message;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.ScreenManager;
import com.cdxy.schoolinforapplication.adapter.MessageListAdapter;
import com.cdxy.schoolinforapplication.model.MessageEntity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.ui.widget.ScrollListView;
import com.cdxy.schoolinforapplication.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.layout_progress)
    LinearLayout layoutProgress;
    @BindView(R.id.scrollListView_mesage_list)
    ScrollListView scrollListViewMesageList;
    @BindView(R.id.activity_message_list)
    LinearLayout activityMessageList;
    private List<MessageEntity> list;
    private MessageListAdapter adapter;
    private String messageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        ButterKnife.bind(this);
        ScreenManager.getScreenManager().pushActivity(this);
        init();
        switch (messageType) {
            case Constant.IMPORTANT_MESSAGE:
                txtTitle.setText("重要消息列表");
                adapter = new MessageListAdapter(MessageListActivity.this, list,Constant.IMPORTANT_MESSAGE);
                scrollListViewMesageList.setAdapter(adapter);
                getImportantMessage();
                break;
            case Constant.NOT_IMPORTANT_MESSAGE:
                txtTitle.setText("普通消息列表");
                adapter = new MessageListAdapter(MessageListActivity.this, list,Constant.NOT_IMPORTANT_MESSAGE);
                scrollListViewMesageList.setAdapter(adapter);
                getNotImportantMessage();
                break;
            case Constant.MY_SEND_MESSAGE:
                txtTitle.setText("我发送的消息列表");
                adapter = new MessageListAdapter(MessageListActivity.this, list,Constant.MY_SEND_MESSAGE);
                scrollListViewMesageList.setAdapter(adapter);
                getMySendMessage();
                break;
        }
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        messageType = intent.getStringExtra("message_type");
        list = new ArrayList<>();
    }

    private void getImportantMessage() {
        new AsyncTask<Void, Void, List<MessageEntity>>() {
            @Override
            protected List<MessageEntity> doInBackground(Void... voids) {
                //测试数据
                MessageEntity entity1 = new MessageEntity(1, "张三", "2016-12-27", "计算机系", "", "期末考试安排", "2017-01-04期末考试结束");
                MessageEntity entity2 = new MessageEntity(1, "李四", "2016-12-22", "网工", "", "寒假放假安排", "放假时间2017-01-07——2017-02-22");
                List<MessageEntity> entityList = new ArrayList<>();
                entityList.add(entity1);
                entityList.add(entity2);
                return entityList;
            }

            @Override
            protected void onPostExecute(List<MessageEntity> messageEntities) {
                super.onPostExecute(messageEntities);
                if (list!=null) {
                    list.addAll(messageEntities);
                    adapter.notifyDataSetChanged();
                    layoutProgress.setVisibility(View.GONE);
                }
            }
        }.execute();
    }

    private void getNotImportantMessage() {
        new AsyncTask<Void, Void, List<MessageEntity>>() {
            @Override
            protected List<MessageEntity> doInBackground(Void... voids) {
                //测试数据
                MessageEntity entity1 = new MessageEntity(0, "zhang", "2016-12-23", "航空分院", "", "实习推荐", "国腾园招软件实习生");
                MessageEntity entity2 = new MessageEntity(0, "li", "2016-12-21", "云计算", "", "小春熙又开新店了", "小春熙大冬天开了一个冰淇淋店，听说去的人还很多");
                List<MessageEntity> entityList = new ArrayList<>();
                entityList.add(entity1);
                entityList.add(entity2);
                return entityList;
            }

            @Override
            protected void onPostExecute(List<MessageEntity> messageEntities) {
                super.onPostExecute(messageEntities);
                if (list!=null) {
                    list.addAll(messageEntities);
                    adapter.notifyDataSetChanged();
                    layoutProgress.setVisibility(View.GONE);
                }
            }
        }.execute();
    }

    private void getMySendMessage() {
        new AsyncTask<Void, Void, List<MessageEntity>>() {
            @Override
            protected List<MessageEntity> doInBackground(Void... voids) {
                //测试数据
                MessageEntity entity1 = new MessageEntity(1, "菊花姐姐", "2016-12-25", "计算机系", "", "期末考试教室变化", "二教205的考生请到206考试");
                MessageEntity entity2 = new MessageEntity(0, "菊花姐姐", "2016-12-29", "计算机系", "", "菊花姐姐要办考研班了", "有想要报名的尽快来报名");
                List<MessageEntity> entityList = new ArrayList<>();
                entityList.add(entity1);
                entityList.add(entity2);
                return entityList;
            }

            @Override
            protected void onPostExecute(List<MessageEntity> messageEntities) {
                super.onPostExecute(messageEntities);
                if (list!=null) {
                    list.addAll(messageEntities);
                    adapter.notifyDataSetChanged();
                    layoutProgress.setVisibility(View.GONE);
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;
        }
    }
}
