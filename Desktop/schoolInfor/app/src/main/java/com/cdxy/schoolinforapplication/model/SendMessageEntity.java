package com.cdxy.schoolinforapplication.model;

import java.util.List;

/**
 * Created by huihui on 2017/4/23.
 */

public class SendMessageEntity {
    private String title;
    private String content;
    private int messageType;
    private List<String> sendTo;

    public SendMessageEntity() {
    }

    public SendMessageEntity(String title, String content, int messageType, List<String> sendTo) {
        this.title = title;
        this.content = content;
        this.messageType = messageType;
        this.sendTo = sendTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "SendMessageEntity{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", messageType=" + messageType +
                ", sendTo=" + sendTo +
                '}';
    }
}
