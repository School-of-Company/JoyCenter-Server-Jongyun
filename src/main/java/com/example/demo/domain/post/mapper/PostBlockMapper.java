package com.example.demo.domain.post.mapper;

import com.example.demo.domain.attachments.entity.AttachmentsEntity;
import com.example.demo.domain.attachments.exception.NotFoundAttachmentException;
import com.example.demo.domain.attachments.repository.AttachmentsRepository;
import com.example.demo.domain.post.entity.PostBlockEntity;
import com.example.demo.domain.post.entity.PostEntity;
import com.example.demo.domain.post.entity.constant.BlockType;
import com.example.demo.domain.post.presentation.data.request.UpdatePostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostBlockMapper {

    private final AttachmentsRepository attachmentsRepository;

    public List<PostBlockEntity> toEntities(
            List<UpdatePostRequest.ContentRequest> contents,
            PostEntity post
    ) {
        Map<Long, AttachmentsEntity> attachmentsMap = loadAttachments(contents);

        return contents.stream()
                .map(content -> toEntity(content, post, attachmentsMap))
                .toList();
    }

    private PostBlockEntity toEntity(
            UpdatePostRequest.ContentRequest content,
            PostEntity post,
            Map<Long, AttachmentsEntity> attachmentsMap
    ) {
        PostBlockEntity.PostBlockEntityBuilder builder = PostBlockEntity.builder()
                .post(post)
                .blockOrder(content.order())
                .blockType(content.type());

        if (content.type() == BlockType.TEXT) {
            builder.text(content.text());
        }

        if (content.type() == BlockType.IMAGE) {
            AttachmentsEntity attachment =
                    attachmentsMap.get(content.attachmentId());

            if (attachment == null) {
                throw new NotFoundAttachmentException();
            }

            builder.attachments(attachment);
        }

        return builder.build();
    }

    private Map<Long, AttachmentsEntity> loadAttachments(
            List<UpdatePostRequest.ContentRequest> contents
    ) {
        Set<Long> attachmentIds = contents.stream()
                .filter(c -> c.type() == BlockType.IMAGE)
                .map(UpdatePostRequest.ContentRequest::attachmentId)
                .collect(Collectors.toSet());

        if (attachmentIds.isEmpty()) {
            return Map.of();
        }

        return attachmentsRepository.findAllById(attachmentIds)
                .stream()
                .collect(Collectors.toMap(
                        AttachmentsEntity::getId,
                        a -> a
                ));
    }
}
