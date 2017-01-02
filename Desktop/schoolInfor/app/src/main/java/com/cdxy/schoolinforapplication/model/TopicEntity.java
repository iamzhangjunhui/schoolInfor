package com.cdxy.schoolinforapplication.model;

import java.util.List;

/**
 * Created by huihui on 2016/12/30.
 */

public class TopicEntity {
    private String id;//发起话题人的id
    private Object icon;//头像
    private String nickName;//昵称
    private String create_time;//发起时间
    private String content;//内容
    private List<Object> photos;//照片
    private List<String> thumbPersonsNickname;//点赞人姓名
    private List<CommentPerson> commentPersons;//评论

    public TopicEntity(String id, Object icon, String nickName, String create_time, String content, List<Object> photos, List<String> thumbPersonsNickname, List<CommentPerson> commentPersons) {
        this.id = id;
        this.icon = icon;
        this.nickName = nickName;
        this.create_time = create_time;
        this.content = content;
        this.photos = photos;
        this.thumbPersonsNickname = thumbPersonsNickname;
        this.commentPersons = commentPersons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Object> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Object> photos) {
        this.photos = photos;
    }

    public List<String> getThumbPersonsNickname() {
        return thumbPersonsNickname;
    }

    public void setThumbPersonsNickname(List<String> thumbPersonsNickname) {
        this.thumbPersonsNickname = thumbPersonsNickname;
    }

    public List<CommentPerson> getCommentPersons() {
        return commentPersons;
    }

    public void setCommentPersons(List<CommentPerson> commentPersons) {
        this.commentPersons = commentPersons;
    }

    @Override
    public String toString() {
        return "TopicEntity{" +
                "id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", nickName='" + nickName + '\'' +
                ", create_time='" + create_time + '\'' +
                ", content='" + content + '\'' +
                ", photos=" + photos +
                ", thumbPersonsNickname=" + thumbPersonsNickname +
                ", commentPersons=" + commentPersons +
                '}';
    }
}
