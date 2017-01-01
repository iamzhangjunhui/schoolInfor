package com.cdxy.schoolinforapplication.model;

/**
 * Created by huihui on 2017/1/1.
 */

public class CommentContent {
    private String senderNickname;//发送方昵称
    private String senderName;//发送方姓名
    private String receiverNickname;//接收方昵称
    private String receiverName;//接收方姓名
    private String content;//评论内容

    public CommentContent(String senderNickname, String senderName, String content) {
        this.senderNickname = senderNickname;
        this.senderName = senderName;
        this.content = content;
    }

    public CommentContent(String senderNickname, String senderName, String receiverNickname, String receiverName, String content) {
        this.senderNickname = senderNickname;
        this.senderName = senderName;
        this.receiverNickname = receiverNickname;
        this.receiverName = receiverName;
        this.content = content;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverNickname() {
        return receiverNickname;
    }

    public void setReceiverNickname(String receiverNickname) {
        this.receiverNickname = receiverNickname;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentContent{" +
                "senderNickname='" + senderNickname + '\'' +
                ", senderName='" + senderName + '\'' +
                ", receiverNickname='" + receiverNickname + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
