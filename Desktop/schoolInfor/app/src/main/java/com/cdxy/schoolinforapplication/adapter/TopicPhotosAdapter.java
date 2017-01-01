package com.cdxy.schoolinforapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cdxy.schoolinforapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huihui on 2017/1/1.
 */

public class TopicPhotosAdapter extends BaseAdapter {
    private Context context;
    private List<Object> list;

    public TopicPhotosAdapter(Context context, List<Object> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_topic_img, null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        Object photo=getItem(i);
        Glide.with(context).load(photo).placeholder(R.drawable.loading).into(viewHolder.imgTopicPhoto);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.img_topic_photo)
        ImageView imgTopicPhoto;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
