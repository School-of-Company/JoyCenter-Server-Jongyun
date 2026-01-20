package com.example.demo.domain.post.presentation.data.response;

import com.example.demo.domain.attachments.entity.constant.AttachmentsType;
import com.example.demo.domain.member.entity.MemberEntity;
import com.example.demo.domain.post.entity.constant.BlockType;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Member member,
        List<Block> blocks
) {
    public record Member(Long memberId, String email) {}

    public record Block(
            Long blockId,
            Integer order,
            BlockType type,
            String text,
            Attachment attachment
    ) {}

    public record Attachment(
            Long attachmentId,
            AttachmentsType attachmentsType,
            String attachmentUrl
    ) {}
}
