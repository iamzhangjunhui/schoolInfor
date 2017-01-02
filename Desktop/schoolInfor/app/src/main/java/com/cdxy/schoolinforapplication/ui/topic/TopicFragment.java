package com.cdxy.schoolinforapplication.ui.topic;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.adapter.TopicAdapter;
import com.cdxy.schoolinforapplication.model.CommentContent;
import com.cdxy.schoolinforapplication.model.CommentPerson;
import com.cdxy.schoolinforapplication.model.TopicEntity;
import com.cdxy.schoolinforapplication.ui.base.BaseFragment;
import com.cdxy.schoolinforapplication.ui.widget.ScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopicFragment extends BaseFragment {

    @BindView(R.id.layout_progress)
    LinearLayout layoutProgress;
    @BindView(R.id.scroll_list_topic)
    ScrollListView scrollListTopic;
    private TopicAdapter adapter;
    private List<TopicEntity> list;

    public TopicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        getTopics();
    }

    @Override
    public void init() {
        list = new ArrayList<>();
        adapter = new TopicAdapter(list, getContext());
        scrollListTopic.setAdapter(adapter);
    }
//测试数据
    private void getTopics() {
        new AsyncTask<Void, Void, List<TopicEntity>>() {
            @Override
            protected List<TopicEntity> doInBackground(Void... voids) {
                List<TopicEntity> topicEntities = new ArrayList<TopicEntity>();
                //上传的图片
                List<Object> photos1=new ArrayList<Object>();
                photos1.add(R.drawable.school);
                photos1.add(R.drawable.myinfor_backgroud_photo);
                photos1.add(R.drawable.students);
                photos1.add(R.drawable.bottom_tap_message_true);
                photos1.add(R.drawable.school);
                photos1.add(R.drawable.bottom_tap_topic_true);
                //点赞人姓名
                List<String> thumbPersonsName=new ArrayList<String>();
                thumbPersonsName.add("可可");
                thumbPersonsName.add("张涛");
                thumbPersonsName.add("李四");
                //评论
                List<CommentPerson> commentPersons=new ArrayList<CommentPerson>();
                //第一个评论人
                List<CommentContent> commentContents1=new ArrayList<CommentContent>();
                CommentContent commentContent1=new CommentContent("Rain","可可","kaylee", "张三","确实很好，一起去打羽毛球吧");
                CommentContent commentContent2=new CommentContent( "kaylee", "张三","Rain","可可","好呀，半个小时后楼下见");
                commentContents1.add(commentContent1);
                commentContents1.add(commentContent2);
                CommentPerson commentPerson1=new CommentPerson("1001",commentContents1);
                //第二个评论人
                List<CommentContent> commentContents2=new ArrayList<CommentContent>();
                CommentContent commentContent3=new CommentContent("Andy","张涛", "kaylee", "张三","我也想打羽毛球了，等一下叫我哦");
                CommentContent commentContent4=new CommentContent( "kaylee", "张三","Andy","张涛","要得，好久没和你PK了");
                commentContents2.add(commentContent3);
                commentContents2.add(commentContent4);
                CommentPerson commentPerson2=new CommentPerson("1002",commentContents2);
                //第三个评论人（我自己）
                List<CommentContent> commentContents3=new ArrayList<CommentContent>();
                CommentContent commentContent5=new CommentContent( "kaylee", "张三","今天玩得好开心");
                commentContents3.add(commentContent5);
                CommentPerson commentPerson3=new CommentPerson("1",commentContents3);
                commentPersons.add(commentPerson1);
                commentPersons.add(commentPerson2);
                commentPersons.add(commentPerson3);
                TopicEntity topicEntity1 = new TopicEntity("1", R.drawable.students, "kaylee", "张三", "2016-12-12", "今天天气好，适合出去玩", photos1, thumbPersonsName, commentPersons);
                TopicEntity topicEntity2 = new TopicEntity("2",R.drawable.students, "andy", "李四", "2016-12-31", "走去打球", null, null, null);
                TopicEntity topicEntity3= new TopicEntity("2",R.drawable.students, "andy", "李四", "2016-12-31", "走去打球", null, null, null);
                topicEntities.add(topicEntity1);
                topicEntities.add(topicEntity2);
                topicEntities.add(topicEntity3);
                return topicEntities;
            }

            @Override
            protected void onPostExecute(List<TopicEntity> topicEntities) {
                super.onPostExecute(topicEntities);
                if (topicEntities != null) {
                    list.addAll(topicEntities);
                    adapter.notifyDataSetChanged();
                    layoutProgress.setVisibility(View.GONE);
                }
            }
        }.execute();
    }
}
