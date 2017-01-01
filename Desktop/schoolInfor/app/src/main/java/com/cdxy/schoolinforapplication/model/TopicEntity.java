package com.cdxy.schoolinforapplication.model;

import java.util.List;

/**
 * Created by huihui on 2016/12/30.
 */

public class TopicEntity {
    private String id;//发起话题人的id
    private Object icon;//头像
    private String nickName;//昵称
    private String name;//姓名
    private String create_time;//发起时间
    private String content;//内容
    private List<Object> photos;//照片
    private int thumbNum;//点赞数
    private List<String> thumbPersonsName;//点赞人姓名
    private List<CommentPerson> commentPersons;//评论

    public TopicEntity(String id, Object icon, String nickName, String name, String create_time, String content, List<Object> photos, int thumbNum, List<String> thumbPersonsName, List<CommentPerson> commentPersons) {
        this.id = id;
        this.icon = icon;
        this.nickName = nickName;
        this.name = name;
        this.create_time = create_time;
        this.content = content;
        this.photos = photos;
        this.thumbNum = thumbNum;
        this.thumbPersonsName = thumbPersonsName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getThumbNum() {
        return thumbNum;
    }

    public void setThumbNum(int thumbNum) {
        this.thumbNum = thumbNum;
    }

    public List<String> getThumbPersonsName() {
        return thumbPersonsName;
    }

    public void setThumbPersonsName(List<String> thumbPersonsName) {
        this.thumbPersonsName = thumbPersonsName;
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
                ", name='" + name + '\'' +
                ", create_time='" + create_time + '\'' +
                ", content='" + content + '\'' +
                ", photos=" + photos +
                ", thumbNum='" + thumbNum + '\'' +
                ", thumbPersonsName=" + thumbPersonsName +
                ", commentPersons=" + commentPersons +
                '}';
    }
}
