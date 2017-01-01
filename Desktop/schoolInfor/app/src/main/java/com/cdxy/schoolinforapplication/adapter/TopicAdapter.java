package com.cdxy.schoolinforapplication.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.model.TopicEntity;
import com.cdxy.schoolinforapplication.ui.widget.ScrollListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by huihui on 2016/12/30.
 */

public class TopicAdapter extends BaseAdapter {
    private List<TopicEntity> list;
    private Context context;

    public TopicAdapter(List<TopicEntity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 :list.size();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_topic, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        TopicEntity entity = (TopicEntity) getItem(i);
        String name = entity.getName();
        String nickName = entity.getNickName();
        if (!TextUtils.isEmpty(nickName)) {
            viewHolder.txtTopicNameOrNickname.setText(nickName);
        } else if (!TextUtils.isEmpty(name)) {
            viewHolder.txtTopicNameOrNickname.setText(name);
        }
        String createTime = entity.getCreate_time();
        if (!TextUtils.isEmpty(createTime)) {
            viewHolder.txtTopicCreateTime.setText(createTime);
        }
        Object icon = entity.getIcon();
        if (icon != null) {
            //这儿的头像类型要分几种（int（drawable），网络图片）
            Glide.with(context).load(icon).placeholder(R.drawable.loading).bitmapTransform(new CropCircleTransformation(context)).into(viewHolder.imgTopicIcon);
        }
        String topicContent = entity.getContent();
        if (!TextUtils.isEmpty(topicContent)) {
            viewHolder.txtTopicContent.setText(topicContent);
        }
        int thumbNumber = entity.getThumbNum();
        if (thumbNumber > 0) {
            viewHolder.layoutThumb.setVisibility(View.VISIBLE);
            viewHolder.txtThumbNumber.setText(thumbNumber + "");
            List<String> thumbPersonsName = entity.getThumbPersonsName();
            if (thumbPersonsName != null) {
                String thumbPersonsNameString = "";
                for (String thumbPersonName : thumbPersonsName) {
                    thumbPersonsNameString = thumbPersonsNameString + "  " + thumbPersonName;
                }
                viewHolder.txtThumbPersonsName.setText(thumbPersonsNameString);
            }
        }
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.img_topic_icon)
        ImageView imgTopicIcon;
        @BindView(R.id.txt_topic_name_or_nickname)
        TextView txtTopicNameOrNickname;
        @BindView(R.id.txt_topic_create_time)
        TextView txtTopicCreateTime;
        @BindView(R.id.txt_topic_content)
        TextView txtTopicContent;
        @BindView(R.id.gridView_photos)
        GridView gridViewPhotos;
        @BindView(R.id.txt_more_photo_number)
        TextView txtMorePhotoNumber;
        @BindView(R.id.img_comment)
        ImageView imgComment;
        @BindView(R.id.img_thumb)
        ImageView imgThumb;
        @BindView(R.id.txt_thumb_number)
        TextView txtThumbNumber;
        @BindView(R.id.txt_thumb_persons_name)
        TextView txtThumbPersonsName;
        @BindView(R.id.layout_thumb)
        LinearLayout layoutThumb;
        @BindView(R.id.scroll_comments)
        ScrollListView scrollComments;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;

    }
}
