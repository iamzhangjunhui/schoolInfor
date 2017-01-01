package com.cdxy.schoolinforapplication.model;

import java.util.List;

/**
 * Created by huihui on 2016/12/30.
 */
public class CommentPerson {
    private String id;//评论者id;
  private List<CommentContent> commentContents;

    public CommentPerson(String id, List<CommentContent> commentContents) {
        this.id = id;
        this.commentContents = commentContents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CommentContent> getCommentContents() {
        return commentContents;
    }

    public void setCommentContents(List<CommentContent> commentContents) {
        this.commentContents = commentContents;
    }

    public CommentPerson() {
        super();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
