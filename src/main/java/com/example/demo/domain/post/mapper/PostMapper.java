package com.example.demo.domain.post.mapper;

import com.example.demo.domain.attachments.entity.AttachmentsEntity;
import com.example.demo.domain.attachments.exception.NotFoundAttachmentException;
import com.example.demo.domain.attachments.repository.AttachmentsRepository;
import com.example.demo.domain.member.entity.MemberEntity;
import com.example.demo.domain.post.entity.PostBlockEntity;
import com.example.demo.domain.post.entity.PostEntity;
import com.example.demo.domain.post.entity.constant.BlockType;
import com.example.demo.domain.post.presentation.data.request.CreatePostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final AttachmentsRepository attachmentsRepository;

    public PostEntity toPostEntity(MemberEntity member, CreatePostRequest request) {
        return PostEntity.builder()
                .author(member)
                .authorName(member.getEmail())
                .title(request.title())
                .build();
    }

    public List<PostBlockEntity> toPostBlocks(
            List<CreatePostRequest.ContentRequest> contents,
            PostEntity post
    ) {
        Set<Long> attachmentIds = contents.stream()
                .filter(c -> c.type() == BlockType.IMAGE)
                .map(CreatePostRequest.ContentRequest::attachmentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, AttachmentsEntity> attachmentMap = attachmentIds.isEmpty()
                ? Map.of()
                : attachmentsRepository.findAllById(attachmentIds).stream()
                .collect(Collectors.toMap(AttachmentsEntity::getId, a -> a));

        return contents.stream()
                .map(c -> toPostBlock(c, post, attachmentMap))
                .toList();
    }

    private PostBlockEntity toPostBlock(
            CreatePostRequest.ContentRequest content,
            PostEntity post,
            Map<Long, AttachmentsEntity> attachmentMap
    ) {
        PostBlockEntity.PostBlockEntityBuilder builder = PostBlockEntity.builder()
                .post(post)
                .blockOrder(content.order())
                .blockType(content.type());

        if (content.type() == BlockType.TEXT) {
            return builder.text(content.text()).build();
        }

        AttachmentsEntity attachment = attachmentMap.get(content.attachmentId());
        if (attachment == null) {
            throw new NotFoundAttachmentException();
        }

        return builder.attachments(attachment).build();
    }
}

