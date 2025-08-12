package com.back.domain.post.postComment.dto;

import com.back.domain.post.postComment.entity.PostComment;

import java.time.LocalDateTime;

public class PostCommentDto {
    private final int id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final int authorId;
    private final String authorName;
    private final int postId;
    private final String content;

    public PostCommentDto(PostComment postComment) {
        id = postComment.getId();
        createDate = postComment.getCreateDate();
        modifyDate = postComment.getModifyDate();
        authorId = postComment.getAuthor().getId();
        authorName = postComment.getAuthor().getName();
        postId = postComment.getPost().getId();
        content = postComment.getContent();
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }
}