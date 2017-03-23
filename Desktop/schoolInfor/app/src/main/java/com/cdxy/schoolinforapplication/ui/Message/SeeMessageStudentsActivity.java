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
import com.cdxy.schoolinforapplication.adapter.message.SeeMessageStudentAdapter;
import com.cdxy.schoolinforapplication.model.message.SeeMeaaseStudentEntity;
import com.cdxy.schoolinforapplication.ui.base.BaseActivity;
import com.cdxy.schoolinforapplication.ui.widget.ScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeeMessageStudentsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.layout_show_progressBar)
    LinearLayout layoutShowProgressBar;
    @BindView(R.id.txt_see_message_student_number)
    TextView txtSeeMessageStudentNumber;
    @BindView(R.id.scroll_see_message_student)
    ScrollListView scrollSeeMessageStudent;
    @BindView(R.id.activity_see_message_students)
    LinearLayout activitySeeMessageStudents;
    private SeeMessageStudentAdapter adapter;
    private List<SeeMeaaseStudentEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_message_students);
        ScreenManager.getScreenManager().pushActivity(this);
        ButterKnife.bind(this);
        init();
        getSeeMessageStudents();


    }

    @Override
    public void init() {
        txtTitle.setText("查看情况");
        btnRight.setText("未查看提醒");
        list = new ArrayList<>();
        adapter = new SeeMessageStudentAdapter(SeeMessageStudentsActivity.this, list);
        scrollSeeMessageStudent.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                ScreenManager.getScreenManager().popActivty(this);
                break;
            case R.id.btn_right:
                Intent intent=new Intent(SeeMessageStudentsActivity.this,NotSeeMessageStudentsActivity.class);
                intent.putExtra("messageEntity",getIntent().getSerializableExtra("messageEntity"));
                startActivity(intent);
        }
    }


    //测试数据
    private void getSeeMessageStudents() {
        new AsyncTask<Void, Void, List<SeeMeaaseStudentEntity>>() {
            @Override
            protected List<SeeMeaaseStudentEntity> doInBackground(Void... voids) {
                List<SeeMeaaseStudentEntity> seeMeaaseStudentEntities = new ArrayList<>();
                SeeMeaaseStudentEntity entity1 = new SeeMeaaseStudentEntity("zhang", "1340610232", "计算机系", "嵌入式1班", "2016-12-27");
                SeeMeaaseStudentEntity entity2 = new SeeMeaaseStudentEntity("li", "1340610222", "计算机系", "软件技术1班", "2016-12-21");
                seeMeaaseStudentEntities.add(entity1);
                seeMeaaseStudentEntities.add(entity2);
                return seeMeaaseStudentEntities;
            }

            @Override
            protected void onPostExecute(List<SeeMeaaseStudentEntity> seeMeaaseStudentEntities) {
                super.onPostExecute(seeMeaaseStudentEntities);
                if (seeMeaaseStudentEntities != null) {
                    list.clear();
                    list.addAll(seeMeaaseStudentEntities);
                    adapter.notifyDataSetChanged();
                    layoutShowProgressBar.setVisibility(View.GONE);
                    txtSeeMessageStudentNumber.setText(seeMeaaseStudentEntities.size()+"");
                }
            }
        }.execute();
    }
}
