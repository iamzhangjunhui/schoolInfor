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

    private void getTopics() {
        new AsyncTask<Void, Void, List<TopicEntity>>() {
            @Override
            protected List<TopicEntity> doInBackground(Void... voids) {
                List<TopicEntity> topicEntities = new ArrayList<TopicEntity>();
                TopicEntity topicEntity1 = new TopicEntity("1", R.drawable.students, "kaylee", "张三", "2016-12-12", "今天天气好，适合出去玩", null, 2, null, null);

                TopicEntity topicEntity2 = new TopicEntity("2",R.drawable.students, "andy", "李四", "2016-12-31", "走去打球", null, 0, null, null);
                topicEntities.add(topicEntity1);
                topicEntities.add(topicEntity2);
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
