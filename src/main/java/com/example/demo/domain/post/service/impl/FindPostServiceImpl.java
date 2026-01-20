package com.example.demo.domain.post.service.impl;

import com.example.demo.domain.post.entity.PostBlockEntity;
import com.example.demo.domain.post.entity.PostEntity;
import com.example.demo.domain.post.entity.constant.BlockType;
import com.example.demo.domain.post.exception.NotFoundPostException;
import com.example.demo.domain.post.presentation.data.response.PostResponse;
import com.example.demo.domain.post.repository.PostBlockRepository;
import com.example.demo.domain.post.repository.PostRepository;
import com.example.demo.domain.post.service.FindPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindPostServiceImpl implements FindPostService {

    private final PostRepository postRepository;
    private final PostBlockRepository postBlockRepository;

    @Override
    public PostResponse execute(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(NotFoundPostException::new);

        List<PostBlockEntity> blocks =
                postBlockRepository.findAllByPostWithAttachments(post);

        return new PostResponse(
                post.getTitle(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                new PostResponse.Member(
                        post.getAuthor().getId(),
                        post.getAuthorName()
                ),
                blocks.stream()
                        .map(this::toBlockResponse)
                        .toList()
        );
    }

    private PostResponse.Block toBlockResponse(PostBlockEntity block) {
        return new PostResponse.Block(
                block.getId(),
                block.getBlockOrder(),
                block.getBlockType(),
                block.getBlockType() == BlockType.TEXT
                        ? block.getText()
                        : null,
                block.getBlockType() == BlockType.IMAGE && block.getAttachments() != null
                        ? new PostResponse.Attachment(
                        block.getAttachments().getId(),
                        block.getAttachments().getAttachmentsType(),
                        block.getAttachments().getAttachmentsUrl()
                )
                        : null
        );
    }
}


