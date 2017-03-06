package com.cdxy.schoolinforapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.model.topic.CommentContent;
import com.cdxy.schoolinforapplication.model.topic.CommentPerson;
import com.cdxy.schoolinforapplication.ui.widget.ScrollListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huihui on 2016/12/30.
 */
//话题列表评论详情中评论人的列表
public class TopicCommentPersonsAdapter extends BaseAdapter {
    private Context context;
    private List<CommentPerson> list;
    private TopicCommentContentAdapter commentContentAdapter;

    public TopicCommentPersonsAdapter(Context context, List<CommentPerson> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_topic_comment, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CommentPerson commentPerson = (CommentPerson) getItem(i);
        List<CommentContent> commentContents = commentPerson.getCommentContents();
        if (commentContents != null) {
            commentContentAdapter = new TopicCommentContentAdapter(context, commentContents);
            viewHolder.scrollCommentPersons.setAdapter(commentContentAdapter);
        }
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.scroll_comment_persons)
        ScrollListView scrollCommentPersons;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
