package com.cdxy.schoolinforapplication.model;

/**
 * Created by huihui on 2017/1/1.
 */

public class CommentContent {
    private String senderNickname;//发送方昵称
    private String receiverNickname;//接收方昵称
    private String content;//评论内容

    public CommentContent(String senderNickname,  String content) {
        this.senderNickname = senderNickname;
        this.content = content;
    }

    public CommentContent(String senderNickname, String receiverNickname, String content) {
        this.senderNickname = senderNickname;
        this.receiverNickname = receiverNickname;
        this.content = content;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getReceiverNickname() {
        return receiverNickname;
    }

    public void setReceiverNickname(String receiverNickname) {
        this.receiverNickname = receiverNickname;
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
                ", receiverNickname='" + receiverNickname + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
