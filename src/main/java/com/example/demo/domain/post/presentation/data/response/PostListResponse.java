package com.example.demo.domain.post.presentation.data.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record PostListResponse(
        List<Item> items,

        PageInfo pageInfo
) {
    public record Item(
            Long id,

            String title,

            Member member,

            LocalDateTime createdAt,

            Thumbnail thumbnail
    ) {}

    public record Member(
            Long memberId,

            String email
    ) {}

    public record Thumbnail(
            Long attachmentsId,

            String url
    ) {}

    public record PageInfo(
            int number,

            int size,

            long totalElements,

            int totalPages,

            boolean hasNext,

            boolean hasPrevious
    ) {}
}