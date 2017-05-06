package com.cdxy.schoolinforapplication.model.topic;

/**
 * Created by huihui on 2017/5/6.
 */

public class CommentEntity {
    private String commentid;   //commentid不用上传，数据库会自动生成，后面删讨论的时候要用到
    private String topicid;
    private String senderNickname;
    private String receiverNickname;
    private String content;

    public CommentEntity( String topicid, String senderNickname, String receiverNickname, String content) {
        this.topicid = topicid;
        this.senderNickname = senderNickname;
        this.receiverNickname = receiverNickname;
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentEntity{" +
                "commentid='" + commentid + '\'' +
                ", topicid='" + topicid + '\'' +
                ", senderNickname='" + senderNickname + '\'' +
                ", receiverNickname='" + receiverNickname + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
