package com.back.domain.post.post.dto;

import com.back.domain.post.post.entity.Post;

public class PostWithContentDto extends PostDto {
    private final String content;

    public PostWithContentDto(Post post) {
        super(post);
        this.content = post.getContent();
    }

    public String getContent() {
        return content;
    }
}