package com.cdxy.schoolinforapplication.model;

/**
 * Created by huihui on 2016/12/30.
 */
public class CommentPerson {
    private String id;
    private String nickname;
    private String name;
    private String content;

    public CommentPerson(String id, String nickname, String name, String content) {
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentPerson{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
