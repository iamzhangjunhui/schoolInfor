package com.cdxy.schoolinforapplication.adapter.topic;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cdxy.schoolinforapplication.HttpUrl;
import com.cdxy.schoolinforapplication.R;
import com.cdxy.schoolinforapplication.model.ReturnEntity;
import com.cdxy.schoolinforapplication.model.UserInfor.UserInforEntity;
import com.cdxy.schoolinforapplication.model.topic.CommentEntity;
import com.cdxy.schoolinforapplication.model.topic.ReturnCommentEntity;
import com.cdxy.schoolinforapplication.model.topic.ReturnThumb;
import com.cdxy.schoolinforapplication.model.topic.ThumbEntity;
import com.cdxy.schoolinforapplication.model.topic.TopicEntity;
import com.cdxy.schoolinforapplication.ui.chat.MyFriendActivity;
import com.cdxy.schoolinforapplication.ui.topic.ShowBigPhotosActivity;
import com.cdxy.schoolinforapplication.ui.widget.NotifyDialog;
import com.cdxy.schoolinforapplication.ui.widget.ScrollListView;
import com.cdxy.schoolinforapplication.util.Constant;
import com.cdxy.schoolinforapplication.util.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by huihui on 2016/12/30.
 */

public class TopicAdapter extends BaseAdapter {
    private List<TopicEntity> list;
    private Activity activity;
    private TopicPhotosAdapter topicPhotosAdapter;
    private TopicCommentContentAdapter topicCommentContentAdapter;
    private LinearLayout layoutAddComment;
    private EditText edtAddComment;
    private TextView txtSendNewComment;
    private Gson gson = new Gson();
    ViewHolder viewHolder;
    UserInforEntity userInfor;
    String myNikename;
    String myUserid;
    List<ReturnCommentEntity> comments;
    private NotifyDialog notifyDialog;

    public TopicAdapter(List<TopicEntity> list, Activity activity, LinearLayout layoutAddComment, EditText edtAddComment, TextView txtSendNewComment) {
        this.list = list;
        this.activity = activity;
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
//        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_topic, null);
            viewHolder = new ViewHolder(view);
            viewHolder.layout_divider = (LinearLayout) view.findViewById(R.id.layout_divider);
            view.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) view.getTag();
//        }
        final TopicEntity entity = (TopicEntity) getItem(i);
        final String nickName = entity.getNickName();
        final String topicid = entity.getTopicid();

        userInfor = SharedPreferenceManager.instance(activity).getUserInfor();
        if (userInfor != null) {
            myNikename = userInfor.getNicheng();
            myUserid=userInfor.getUserid();
        }

        if (!TextUtils.isEmpty(topicid)) {
//            //获取话题图片
//            getTopicPhoto(topicid);
            //获取点赞人
            getAllThumb(entity);
            //获取评论列表
            getComments(topicid);
        }
        //加载图片
        final List<Object> photos=entity.getPhotos();
        if (photos!=null){
            if (photos.size()!=0){
                viewHolder.framePhotos.setVisibility(View.VISIBLE);
                topicPhotosAdapter = new TopicPhotosAdapter(activity, photos);
                viewHolder.gridViewPhotos.setAdapter(topicPhotosAdapter);
                if (photos.size() > 3) {
                    viewHolder.txtMorePhotoNumber.setVisibility(View.VISIBLE);
                    viewHolder.txtMorePhotoNumber.setText("+" + (photos.size() - 4));
                }

                //点击话题图片放大
                viewHolder.gridViewPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(activity, ShowBigPhotosActivity.class);
                        intent.putExtra("position", i);
                        intent.putExtra("photos", (Serializable) photos);
                        intent.putExtra("photosType", Constant.SHOW_BIG_PHOTO_TOPIC_FRAGMENT);
                        activity.startActivity(intent);
                    }
                });
            }
        }
        //设置评论的适配器
        comments = new ArrayList<>();
        topicCommentContentAdapter = new TopicCommentContentAdapter(activity, comments);
        viewHolder.scrollComments.setAdapter(topicCommentContentAdapter);
        //长按一条自己发送的评论实现删除
        viewHolder.scrollComments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });

        //判断该话题是否是本人发的，如果是的话就可以删除
        if (entity.getUserid().equals(userInfor.getUserid())) {
            viewHolder.imgDelete.setVisibility(View.VISIBLE);
            viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createNotifyDailog(entity.getTopicid(),i);
                }
            });
        }else {
            viewHolder.imgDelete.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(nickName)) {
            viewHolder.txtTopicNickname.setText(nickName);
            String createTime = entity.getCreate_time();
            if (!TextUtils.isEmpty(createTime)) {
            }
            viewHolder.txtTopicCreateTime.setText(createTime);
        }
        Object icon = entity.getIcon();
        if (icon != null) {
            //这儿的头像类型要分几种（int（drawable），网络图片）
            Glide.with(activity).load(icon).placeholder(R.drawable.loading).bitmapTransform(new CropCircleTransformation(activity)).into(viewHolder.imgTopicIcon);
        }
        String topicContent = entity.getContent();
        if (!TextUtils.isEmpty(topicContent)) {
            viewHolder.txtTopicContent.setText(topicContent);
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
            } else {
                viewHolder.txtThumbPersonsNickname.setVisibility(View.VISIBLE);
                viewHolder.layout_divider.setVisibility(View.VISIBLE);
            }
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
                        }
                        String topicid = entity.getTopicid();
                        String receiverNickname = entity.getNickName();
                        if (!TextUtils.isEmpty(myNikename) && (!TextUtils.isEmpty(receiverNickname))) {
                            CommentEntity commentEntity = new CommentEntity(topicid, myNikename, receiverNickname, newCommentcontent);
                            String json = gson.toJson(commentEntity);
                            sendComment(json, topicid);
                            layoutAddComment.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        viewHolder.imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(myNikename)&&(!TextUtils.isEmpty(myUserid))) {
                    boolean iHasThumb = entity.isiHasThumb();
                    String topicid = entity.getTopicid();

                    if (iHasThumb) {
                        //取消点赞
                        notThumb(topicid,myUserid, entity);
                    } else {
                        //点赞
                        ThumbEntity thumbEntity = new ThumbEntity(topicid, myUserid, myNikename);
                        String jsonString = gson.toJson(thumbEntity);
                        thumb(jsonString, entity);
                    }


                }else {
                    Toast.makeText(activity,"你还没设置昵称，快去设置吧",Toast.LENGTH_SHORT).show();
                }
                TopicAdapter.this.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;

    }
    //获取点赞人列表
    private void getAllThumb(String topicid, final TopicEntity topicEntity) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpUrl.All_TOPIC_THUMBS + "?topicid=" + topicid).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Observable.just(result).map(new Func1<String, ReturnEntity<List<ReturnThumb>>>() {
                    @Override
                    public ReturnEntity<List<ReturnThumb>> call(String s) {
                        ReturnEntity<List<ReturnThumb>> returnEntity = gson.fromJson(result, ReturnEntity.class);
                        if (returnEntity != null) {
                            returnEntity = gson.fromJson(result, new TypeToken<ReturnEntity<List<ReturnThumb>>>() {
                            }.getType());
                        }
                        return returnEntity;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ReturnEntity<List<ReturnThumb>>>() {
                    @Override
                    public void call(ReturnEntity<List<ReturnThumb>> listReturnEntity) {
                        if (listReturnEntity != null) {
                            if (listReturnEntity.getCode() == 1) {
                                List<String> thumbs = new ArrayList<String>();
                                for (ReturnThumb thumb : listReturnEntity.getData()) {
                                    thumbs.add(thumb.getUserid());
                                }
                                topicEntity.setThumbPersonsNickname(thumbs);
                                TopicAdapter.this.notifyDataSetChanged();

                            } else {
                                Toast.makeText(activity, listReturnEntity.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        });
    }
//    private void getTopicPhoto(final String topicid) {
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder().url(HttpUrl.ALL_TOPIC_PHOTOS + "?topicid=" +"5435").build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                Log.d("qwqeqewqeq"+topicid,result);
//                Observable.just(result).map(new Func1<String, ReturnEntity<List<Object>>>() {
//                    @Override
//                    public ReturnEntity<List<Object>> call(String s) {
//                        ReturnEntity<List<Object>> returnEntity = gson.fromJson(s, ReturnEntity.class);
//                        if (returnEntity != null) {
//                            returnEntity = gson.fromJson(s, new TypeToken<ReturnEntity<List<Object>>>() {
//                            }.getType());
//                        }
//                        return returnEntity;
//                    }
//                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ReturnEntity<List<Object>>>() {
//                    @Override
//                    public void call(ReturnEntity<List<Object>> listReturnEntity) {
//                        if (listReturnEntity != null) {
//                            if (listReturnEntity.getCode() == 1) {
//                                final List<Object> photos = listReturnEntity.getData();
//                                if (photos != null) {
//                                    if (photos.size() != 0) {
//                                        viewHolder.framePhotos.setVisibility(View.VISIBLE);
//                                        topicPhotosAdapter = new TopicPhotosAdapter(activity, photos);
//                                        viewHolder.gridViewPhotos.setAdapter(topicPhotosAdapter);
//                                        if (photos.size() > 3) {
//                                            viewHolder.txtMorePhotoNumber.setVisibility(View.VISIBLE);
//                                            viewHolder.txtMorePhotoNumber.setText("+" + (photos.size() - 4));
//                                        }

//                                    }
//                                }
//
//                            } else {
//                                Toast.makeText(activity, listReturnEntity.getMsg(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//            }
//        });
//    }

    //获取评论列表
    public void getComments(String topicid) {
        OkHttpClient okhttpclient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpUrl.ALL_TOPIC_COMMENTS + "?topicid=" + topicid).get().build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Observable.just(result).map(new Func1<String, ReturnEntity<List<ReturnCommentEntity>>>() {
                    @Override
                    public ReturnEntity<List<ReturnCommentEntity>> call(String s) {
                        ReturnEntity<List<ReturnCommentEntity>> returnEntity = gson.fromJson(s, ReturnEntity.class);
                        if (returnEntity != null) {
                            returnEntity = gson.fromJson(s, new TypeToken<ReturnEntity<List<ReturnCommentEntity>>>() {
                            }.getType());
                        }
                        return returnEntity;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ReturnEntity<List<ReturnCommentEntity>>>() {
                    @Override
                    public void call(ReturnEntity<List<ReturnCommentEntity>> listReturnEntity) {
                        if (listReturnEntity != null) {
                            if (listReturnEntity.getCode() == 1) {
                                comments = listReturnEntity.getData();
                                if (comments != null) {
                                    if (comments.size() != 0) {
                                        topicCommentContentAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                Toast.makeText(activity, listReturnEntity.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

    }

    private void thumb(String json, final TopicEntity topicEntity) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpUrl.THUMB + "?likejson=" + json).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                getAllThumb(topicEntity);
                topicEntity.setiHasThumb(true);
            }
        });
    }

    //获取点赞人列表
    private void getAllThumb(final TopicEntity topicEntity) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpUrl.All_TOPIC_THUMBS + "?topicid=" + topicEntity.getTopicid()).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Observable.just(result).map(new Func1<String, ReturnEntity<List<ReturnThumb>>>() {
                    @Override
                    public ReturnEntity<List<ReturnThumb>> call(String s) {
                        ReturnEntity<List<ReturnThumb>> returnEntity = gson.fromJson(result, ReturnEntity.class);
                        if (returnEntity != null) {
                            returnEntity = gson.fromJson(result, new TypeToken<ReturnEntity<List<ReturnThumb>>>() {
                            }.getType());
                        }
                        return returnEntity;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ReturnEntity<List<ReturnThumb>>>() {
                    @Override
                    public void call(ReturnEntity<List<ReturnThumb>> listReturnEntity) {
                        if (listReturnEntity != null) {
                            if (listReturnEntity.getCode() == 1) {
                                List<String> thumbs = new ArrayList<String>();
                                for (ReturnThumb thumb : listReturnEntity.getData()) {
                                    thumbs.add(thumb.getUserid());
                                }
                                topicEntity.setThumbPersonsNickname(thumbs);
                                TopicAdapter.this.notifyDataSetChanged();

                            } else {
                                Toast.makeText(activity, listReturnEntity.getMsg(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        });
    }

    private void notThumb(String topicid,String userid, final TopicEntity topicEntity) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpUrl.NOT_THUMB + "?topicid=" + topicid+"&&userid="+userid).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                getAllThumb(topicEntity);
                topicEntity.setiHasThumb(false);

            }
        });
    }

    private void sendComment(String commentjson, final String topicid) {
        OkHttpClient okhttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpUrl.SEND_COMMENT + "?commentjson=" + commentjson).get().build();
        okhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                getComments(topicid);
            }
        });
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
        @BindView(R.id.img_delete)
        ImageView imgDelete;
        @BindView(R.id.txt_thumb_persons_nickname)
        TextView txtThumbPersonsNickname;
        @BindView(R.id.scroll_comments)
        ScrollListView scrollComments;
        private LinearLayout layout_divider;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private void deleteMyToppic(String topicid, final int position) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(HttpUrl.DELETE_MY_TOPIC + "?topicid=" + topicid).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Observable.just(result).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        list.remove(position);
                        TopicAdapter.this.notifyDataSetChanged();
                    }
                });
            }
        });
    }
    private void createNotifyDailog(final String topicid, final int position) {
        notifyDialog = new NotifyDialog(activity, R.style.MyDialog, new NotifyDialog.NotifyListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.dialog_ok:
                        deleteMyToppic(topicid,position);
                        notifyDialog.dismiss();
                        break;
                    case R.id.dialog_cancle:
                        notifyDialog.dismiss();
                        break;
                }
            }
        }, activity,"delete_my_topic");
        notifyDialog.show();
    }
}
