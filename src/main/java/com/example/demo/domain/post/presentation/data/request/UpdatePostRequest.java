package com.example.demo.domain.post.presentation.data.request;

import com.example.demo.domain.post.entity.constant.BlockType;

import java.util.List;

public record UpdatePostRequest(
        String title,
        List<ContentRequest> contents
) {
    public record ContentRequest(
            Integer order,
            BlockType type,
            String text,
            Long attachmentId
    ) {
    }
}