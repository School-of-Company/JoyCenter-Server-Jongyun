package com.example.demo.domain.post.service.impl;

import com.example.demo.domain.post.entity.PostEntity;
import com.example.demo.domain.post.entity.constant.BlockType;
import com.example.demo.domain.post.entity.constant.Sort;
import com.example.demo.domain.post.presentation.data.response.PostListResponse;
import com.example.demo.domain.post.repository.PostBlockRepository;
import com.example.demo.domain.post.repository.PostRepository;
import com.example.demo.domain.post.service.FindAllPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAllPostServiceImpl implements FindAllPostService {

    private final PostRepository postRepository;
    private final PostBlockRepository postBlockRepository;

    @Override
    public PostListResponse execute(Sort sort, int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort.toSpringSort()
        );

        Page<PostEntity> postPage = postRepository.findAll(pageable);

        List<PostListResponse.Item> items = postPage.getContent().stream()
                .map(this::toItem)
                .toList();

        return new PostListResponse(
                items,
                toPageInfo(postPage)
        );
    }

    private PostListResponse.Item toItem(PostEntity post) {

        PostListResponse.Thumbnail thumbnail =
                postBlockRepository.findAllByPostIdOrderByBlockOrderAsc(post.getId())
                        .stream()
                        .filter(block -> block.getBlockType() == BlockType.IMAGE)
                        .findFirst()
                        .map(block -> new PostListResponse.Thumbnail(
                                block.getAttachments().getId(),
                                block.getAttachments().getAttachmentsUrl()
                        ))
                        .orElse(null);

        return new PostListResponse.Item(
                post.getId(),
                post.getTitle(),
                new PostListResponse.Member(
                        post.getAuthor().getId(),
                        post.getAuthorName()

                ),
                post.getCreatedAt(),
                thumbnail
        );
    }

    private PostListResponse.PageInfo toPageInfo(Page<PostEntity> page) {
        return new PostListResponse.PageInfo(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
