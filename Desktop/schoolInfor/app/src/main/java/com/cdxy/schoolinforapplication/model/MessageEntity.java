package com.cdxy.schoolinforapplication.model;

/**
 * Created by huihui on 2016/12/27.
 */

public class MessageEntity {
    private int type;//消息类型，0为非重要消息，1为重要消息
    private String sender; //发送人
    private String sendTime; //发送时间
    private String acceptGroup;//接收群体
    private String senderIcon;//发送人头像
    private String title;//消息标题
    private String content;//消息详情

    public MessageEntity(int type, String sender, String sendTime, String acceptGroup, String senderIcon, String title, String content) {
        this.type = type;
        this.sender = sender;
        this.sendTime = sendTime;
        this.acceptGroup = acceptGroup;
        this.senderIcon = senderIcon;
        this.title = title;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getAcceptGroup() {
        return acceptGroup;
    }

    public void setAcceptGroup(String acceptGroup) {
        this.acceptGroup = acceptGroup;
    }

    public String getSenderIcon() {
        return senderIcon;
    }

    public void setSenderIcon(String senderIcon) {
        this.senderIcon = senderIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "type=" + type +
                ", sender='" + sender + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", acceptGroup='" + acceptGroup + '\'' +
                ", senderIcon='" + senderIcon + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
