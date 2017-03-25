package com.cdxy.schoolinforapplication.ui.topic;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.adapter.topic.TopicAdapter;
import com.cdxy.schoolinforapplication.model.topic.CommentContent;
import com.cdxy.schoolinforapplication.model.topic.TopicEntity;
import com.cdxy.schoolinforapplication.ui.base.BaseFragment;
import com.cdxy.schoolinforapplication.ui.widget.RefreshLayout;

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
    @BindView(R.id.edt_add_comment)
    EditText edtAddComment;
    @BindView(R.id.layout_add_comment)
    LinearLayout layoutAddComment;
    @BindView(R.id.txt_send_new_comment)
    TextView txtSendNewComment;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.list_topic)
    ListView listTopic;
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void init() {
        list = new ArrayList<>();
        /**
         * 在 refreshLayout.setLoading(false);方法中添加了footerView
         * footerView的添加必须setAdapter之前
         * public void setAdapter(ListAdapter adapter) {
         ........
         if (mHeaderViewInfos.size() > 0|| mFooterViewInfos.size() > 0) {
         mAdapter = new HeaderViewListAdapter(mHeaderViewInfos, mFooterViewInfos, adapter);
         } else {
         mAdapter = adapter;
         }
         ........
         }
         * 通过adapter的setAdapter()方法可以看出
         * 在setAdapter之前要判断该是否存在headView或footerView，如果存在就创建并传HeaderViewListAdapter，否则传adapter
         */
        refreshLayout.setLoading(false);
        adapter = new TopicAdapter(list, getActivity(), layoutAddComment, edtAddComment, txtSendNewComment);
        listTopic.setAdapter(adapter);
        //设置下拉刷新时的颜色值，颜色需要定义在xml中
        refreshLayout.setColorSchemeColors(R.color.top_color, R.color.colorAccent, R.color.text_red_color, R.color.txt_departent_class_color);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast("刷新");
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast("加载");
                        refreshLayout.setLoading(false);
                    }
                }, 2000);
            }
        });
    }

    //测试数据
    private void getTopics() {
        new AsyncTask<Void, Void, List<TopicEntity>>() {
            @Override
            protected List<TopicEntity> doInBackground(Void... voids) {
                List<TopicEntity> topicEntities = new ArrayList<TopicEntity>();
                //上传的图片
                List<Object> photos1 = new ArrayList<Object>();
                photos1.add(R.drawable.school);
                photos1.add(R.drawable.myinfor_backgroud_photo);
                photos1.add(R.drawable.students);
                photos1.add(R.drawable.bottom_tap_message_true);
                photos1.add(R.drawable.school);
                photos1.add(R.drawable.bottom_tap_topic_true);
                //点赞人姓名
                List<String> thumbPersonsNickname = new ArrayList<String>();
                thumbPersonsNickname.add("Rain");
                thumbPersonsNickname.add("Andy");
                //第一个评论人
                List<CommentContent> commentContents1 = new ArrayList<CommentContent>();
                CommentContent commentContent1 = new CommentContent("Rain", "kaylee", "确实很好，一起去打羽毛球吧");
                CommentContent commentContent2 = new CommentContent("kaylee", "Rain", "好呀，半个小时后楼下见");
                commentContents1.add(commentContent1);
                commentContents1.add(commentContent2);
                //第二个评论人
                List<CommentContent> commentContents2 = new ArrayList<CommentContent>();
                CommentContent commentContent3 = new CommentContent("Andy", "kaylee", "我也想打羽毛球了，等一下叫我哦");
                CommentContent commentContent4 = new CommentContent("kaylee", "Andy", "要得，好久没和你PK了");
                commentContents2.add(commentContent3);
                commentContents2.add(commentContent4);
                List<Object> photos = new ArrayList<>();
                photos.add(R.drawable.bottom_tap_chat_false);
                photos.add(R.drawable.bottom_tap_chat_false);
                photos.add(R.drawable.bottom_tap_chat_false);
                photos.add(R.drawable.bottom_tap_chat_false);
                photos.add(R.drawable.bottom_tap_chat_false);
                TopicEntity topicEntity1 = new TopicEntity("1", R.drawable.students, "kaylee", "2016-12-12", "今天天气好，适合出去玩", photos, thumbPersonsNickname, commentContents1);
                TopicEntity topicEntity2 = new TopicEntity("2", R.drawable.students, "andy", "2016-12-31", "走去打球", null, null, commentContents2);
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
