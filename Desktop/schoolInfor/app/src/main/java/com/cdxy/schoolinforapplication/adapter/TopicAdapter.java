package com.cdxy.schoolinforapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.model.topic.CommentContent;
import com.cdxy.schoolinforapplication.model.topic.CommentPerson;
import com.cdxy.schoolinforapplication.model.topic.TopicEntity;
import com.cdxy.schoolinforapplication.ui.topic.ShowBigPhotosActivity;
import com.cdxy.schoolinforapplication.ui.widget.ScrollListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by huihui on 2016/12/30.
 */

public class TopicAdapter extends BaseAdapter implements View.OnClickListener {
    private List<TopicEntity> list;
    private Context context;
    private TopicPhotosAdapter topicPhotosAdapter;
    private TopicCommentPersonsAdapter topicCommentPersonsAdapter;
    private LinearLayout layoutAddComment;
    private EditText edtAddComment;
    private TextView txtSendNewComment;

    public TopicAdapter(List<TopicEntity> list, Context context, LinearLayout layoutAddComment, EditText edtAddComment, TextView txtSendNewComment) {
        this.list = list;
        this.context = context;
        this.layoutAddComment = layoutAddComment;
        this.edtAddComment = edtAddComment;
        this.txtSendNewComment = txtSendNewComment;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_topic, null);
            viewHolder = new ViewHolder(view);
            viewHolder.layout_divider = (LinearLayout) view.findViewById(R.id.layout_divider);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final TopicEntity entity = (TopicEntity) getItem(i);
        final String nickName = entity.getNickName();
        if (!TextUtils.isEmpty(nickName)) {
            viewHolder.txtTopicNickname.setText(nickName);
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
        final List<Object> photos = entity.getPhotos();
        if (photos != null) {
            int width = context.getResources().getDisplayMetrics().widthPixels / 3;
            viewHolder.gridViewPhotos.setColumnWidth(width);
            viewHolder.framePhotos.setVisibility(View.VISIBLE);
            topicPhotosAdapter = new TopicPhotosAdapter(context, photos);
            viewHolder.gridViewPhotos.setAdapter(topicPhotosAdapter);
            if (photos.size() > 3) {
                viewHolder.txtMorePhotoNumber.setVisibility(View.VISIBLE);
                viewHolder.txtMorePhotoNumber.setText("+" + (photos.size() - 3));
            }
            viewHolder.gridViewPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(context, ShowBigPhotosActivity.class);
                    intent.putExtra("position", i);
                    intent.putExtra("photos", (Serializable) photos);
                    context.startActivity(intent);
                }
            });
        }
        //点赞人姓名集合
        final List<String> thumbPersonsName = entity.getThumbPersonsNickname();
        if (thumbPersonsName != null) {
            int thumbNumber = thumbPersonsName.size();
            if (thumbNumber > 0) {
                viewHolder.layout_divider.setVisibility(View.VISIBLE);
                viewHolder.txtThumbPersonsNickname.setVisibility(View.VISIBLE);
                String thumbPersonsNameString = "";
                for (int j = 0; j < thumbPersonsName.size(); j++) {
                    if (j == 0) {
                        thumbPersonsNameString = thumbPersonsName.get(j);
                    } else {
                        thumbPersonsNameString = thumbPersonsNameString + "," + thumbPersonsName.get(j);
                    }
                }
                thumbPersonsNameString = thumbPersonsNameString + " " + thumbNumber + "人为你点赞";
                viewHolder.txtThumbPersonsNickname.setText(thumbPersonsNameString);
            }
        }
        final List<CommentPerson> commentPersons = entity.getCommentPersons();
        if (commentPersons != null) {
            topicCommentPersonsAdapter = new TopicCommentPersonsAdapter(context, commentPersons);
            viewHolder.scrollComments.setAdapter(topicCommentPersonsAdapter);
        }
        viewHolder.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAddComment.setText("");
                layoutAddComment.setVisibility(View.VISIBLE);
                txtSendNewComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String newCommentcontent = edtAddComment.getText().toString();
                        if (TextUtils.isEmpty(newCommentcontent)) {
                            return;
                        } else {
                            //编辑框不再显示
                            layoutAddComment.setVisibility(View.GONE);
                            int i = 0;
                            //该用户是第一个评价这个话题的人
                            for (; i < commentPersons.size(); i++) {
                                if (nickName.equals(commentPersons.get(i))) ;
                                break;
                            }
                            //这是该用户对这个话题的第一个评价
                            if (i == commentPersons.size()) {
                                //新创建一个评论人对象
                                ArrayList<CommentContent> commentContents = new ArrayList<>();
                                //这里的发送方的昵称"小芳"，本应该是用户昵称，这里只是用于测试。
                                CommentContent commentContent = new CommentContent("小芳", nickName, newCommentcontent);
                                commentContents.add(commentContent);
                                //id本应该是从Sharepreference获取，现在暂时随便写一个，作为测试
                                CommentPerson commentPerson = new CommentPerson("121", commentContents);
                                commentPersons.add(commentPerson);
                            } else {
                                //在该评论人的评论列表中添加一条评论。
                                //这里的发送者应该是用户昵称，测试阶段暂时使用从列表中获取评论者姓名
                                String senderNickname = commentPersons.get(i).getCommentContents().get(0).getSenderNickname();
                                CommentContent commentContent = new CommentContent(senderNickname, nickName, newCommentcontent);
                                commentPersons.get(i).getCommentContents().add(commentContent);
                            }
                            topicCommentPersonsAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        viewHolder.imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取点赞人的昵称(应该从SharePreference里面获取用户的昵称)
                String newThumbNickname = entity.getNickName();//这里的不是获取的不是用户昵称，只是测试用
                if (viewHolder.txtThumbPersonsNickname.getVisibility() == View.GONE) {
                    viewHolder.txtThumbPersonsNickname.setVisibility(View.VISIBLE);
                    viewHolder.layout_divider.setVisibility(View.VISIBLE);
                }
                int j = 0;
                for (; j < thumbPersonsName.size(); j++) {
                    if (newThumbNickname.equals(thumbPersonsName.get(j))) {
                        break;
                    }
                }
                //如果这个人还没有点过赞，则添加进点赞人列表中
                if (j == thumbPersonsName.size()) {
                    entity.getThumbPersonsNickname().add(newThumbNickname);
                } else {
                    //如果这个人已点过赞，则将这个人从点赞人列表中移除
                    entity.getThumbPersonsNickname().remove(j--);
                    if (thumbPersonsName.size() == 0) {
                        viewHolder.layout_divider.setVisibility(View.GONE);
                        viewHolder.txtThumbPersonsNickname.setVisibility(View.GONE);
                    }
                }
                TopicAdapter.this.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_comment:
                break;
            case R.id.img_thumb:
                break;
        }
    }


    @Override
    public boolean isEnabled(int position) {
        return false;

    }

    static class ViewHolder {
        @BindView(R.id.img_topic_icon)
        ImageView imgTopicIcon;
        @BindView(R.id.txt_topic_nickname)
        TextView txtTopicNickname;
        @BindView(R.id.txt_topic_create_time)
        TextView txtTopicCreateTime;
        @BindView(R.id.txt_topic_content)
        TextView txtTopicContent;
        @BindView(R.id.gridView_photos)
        GridView gridViewPhotos;
        @BindView(R.id.txt_more_photo_number)
        TextView txtMorePhotoNumber;
        @BindView(R.id.frame_photos)
        FrameLayout framePhotos;
        @BindView(R.id.img_comment)
        ImageView imgComment;
        @BindView(R.id.img_thumb)
        ImageView imgThumb;
        @BindView(R.id.txt_thumb_persons_nickname)
        TextView txtThumbPersonsNickname;
        @BindView(R.id.scroll_comments)
        ScrollListView scrollComments;
        private LinearLayout layout_divider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
