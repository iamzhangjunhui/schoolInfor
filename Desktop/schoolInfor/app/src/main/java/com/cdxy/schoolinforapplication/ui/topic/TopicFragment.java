package com.cdxy.schoolinforapplication.ui.topic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cdxy.schoolinforapplication.HttpUrl;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.adapter.topic.TopicAdapter;
import com.cdxy.schoolinforapplication.model.ReturnEntity;
import com.cdxy.schoolinforapplication.model.topic.ReturnTopicEntity;
import com.cdxy.schoolinforapplication.model.topic.TopicEntity;
import com.cdxy.schoolinforapplication.ui.MainActivity;
import com.cdxy.schoolinforapplication.ui.base.BaseFragment;
import com.cdxy.schoolinforapplication.ui.widget.RefreshLayout;
import com.cdxy.schoolinforapplication.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static java.lang.Thread.sleep;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopicFragment extends BaseFragment {

    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.list_topic)
    ListView listTopic;
    @BindView(R.id.layout_progress)
    LinearLayout layoutProgress;
    @BindView(R.id.edt_add_comment)
    EditText edtAddComment;
    @BindView(R.id.txt_send_new_comment)
    TextView txtSendNewComment;
    @BindView(R.id.layout_add_comment)
    LinearLayout layoutAddComment;
    @BindView(R.id.progress)
    ProgressBar progress;
    private TopicAdapter adapter;
    private List<TopicEntity> list;
    private Gson gson;

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
                try {
                    getAllTopic();
                    sleep(2000);
                    refreshLayout.setRefreshing(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setLoading(false);
                    }
                }, 2000);
            }
        });

        gson = new Gson();
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllTopic();
    }
    private void getAllTopic() {
        list.clear();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    OkHttpClient okHttpClient = HttpUtil.getClient();
                    Request request = new Request.Builder().url(HttpUrl.All_TOPICS).get().build();
                    Response response = okHttpClient.newCall(request).execute();
                    subscriber.onNext(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                ReturnEntity<List<ReturnTopicEntity>> returnEntity = gson.fromJson(s, ReturnEntity.class);
                if (returnEntity != null) {
                    returnEntity = gson.fromJson(s, new TypeToken<ReturnEntity<List<ReturnTopicEntity>>>() {
                    }.getType());
                    if (returnEntity.getCode() == 1) {
                        List<ReturnTopicEntity> returnTopicList = returnEntity.getData();
                        for (ReturnTopicEntity returnTopic : returnTopicList) {
                            String topicid = returnTopic.getTopicid();
                            String userid = returnTopic.getAuthorid();
                            if ((!TextUtils.isEmpty(topicid)) && (!TextUtils.isEmpty(userid))) {
                                TopicEntity topicEntity = new TopicEntity();
                                topicEntity.setContent(returnTopic.getContent());
                                topicEntity.setCreate_time(returnTopic.getCreateTime());
                                topicEntity.setIcon(returnTopic.getIcon());
                                topicEntity.setNickName(returnTopic.getNickName());
                                topicEntity.setUserid(userid);
                                topicEntity.setTopicid(topicid);
                                getTopicPhoto(topicid, topicEntity);
                            }

                        }
                    } else {
                        toast(returnEntity.getMsg());
                    }
                }
            }
        });

    }

    private void getTopicPhoto(final String topicid, final TopicEntity topicEntity) {
        progress.setVisibility(View.VISIBLE);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                OkHttpClient okHttpClient = HttpUtil.getClient();
                Request request = new Request.Builder().url(HttpUrl.ALL_TOPIC_PHOTOS + "?topicid=" + topicid).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    subscriber.onNext(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                ReturnEntity<List<Object>> returnEntity = gson.fromJson(s, ReturnEntity.class);
                if (returnEntity != null) {
                    returnEntity = gson.fromJson(s, new TypeToken<ReturnEntity<List<Object>>>() {
                    }.getType());
                    if (returnEntity.getCode() == 1) {
                        final List<Object> photos = returnEntity.getData();
                        if (photos != null) {
                            if (photos.size() != 0) {
                                topicEntity.setPhotos(photos);
                                adapter.notifyDataSetChanged();
                            }
                        }

                    } else {
                        toast(returnEntity.getMsg());
                    }
                }
                list.add(topicEntity);
                progress.setVisibility(View.GONE);
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
