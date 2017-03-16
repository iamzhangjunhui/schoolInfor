package com.cdxy.schoolinforapplication.ui.Message;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.adapter.ParentAdapter;
import com.cdxy.schoolinforapplication.model.tree.ChildEntity;
import com.cdxy.schoolinforapplication.model.tree.ParentEntity;
import com.cdxy.schoolinforapplication.ui.widget.ScollerExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendMessageActivity extends Activity implements View.OnClickListener, ExpandableListView.OnGroupClickListener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.txt_child_name)
    TextView txtChildName;
    @BindView(R.id.ck_all)
    CheckBox ckAll;
    @BindView(R.id.eList)
    ScollerExpandableListView eList;
    @BindView(R.id.activity_send_message)
    LinearLayout activitySendMessage;
    private ParentAdapter adapter;
    private List<ParentEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ButterKnife.bind(this);
        initData();
        adapter = new ParentAdapter(list, SendMessageActivity.this);
        eList.setAdapter(adapter);
        eList.setOnGroupClickListener(this);
        ckAll.setOnCheckedChangeListener(this);
    }

    private void initData() {
        txtTitle.setText("发送消息");
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            List<ChildEntity> children1 = new ArrayList<>();
            ChildEntity childEntity1 = new ChildEntity(getResources().getColor(R.color.colorPrimary), "嵌入式1班");
            children1.add(childEntity1);
            ChildEntity childEntity2 = new ChildEntity(getResources().getColor(R.color.colorPrimary), "游戏开发1班");
            children1.add(childEntity2);
            ParentEntity parentEntity1 = new ParentEntity(getResources().getColor(R.color.text_color), "计算机系", children1);
            list.add(parentEntity1);
            List<ChildEntity> children2 = new ArrayList<>();
            ChildEntity childEntity3 = new ChildEntity(getResources().getColor(R.color.colorPrimary), "云计算");
            children2.add(childEntity3);
            ParentEntity parentEntity2 = new ParentEntity(getResources().getColor(R.color.text_color), "云计算系", children2);
            list.add(parentEntity2);
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
        if (expandableListView.isGroupExpanded(i)) {
            expandableListView.collapseGroup(i);
            list.get(i).setExpand(false);
        } else {
            expandableListView.expandGroup(i);
            list.get(i).setExpand(true);
        }
        adapter.notifyDataSetChanged();

        return true;
    }

    private void changeAllStatus(boolean isSelect) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelect(isSelect);
            List<ChildEntity> childEntities = list.get(i).getChildren();
            for (int j = 0; j < childEntities.size(); j++) {
                childEntities.get(j).setSelect(isSelect);
            }

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            changeAllStatus(true);
        } else {
            changeAllStatus(false);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
