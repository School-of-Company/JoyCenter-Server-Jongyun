package com.example.demo.domain.post.service.impl;

import com.example.demo.domain.member.entity.MemberEntity;
import com.example.demo.domain.post.entity.PostBlockEntity;
import com.example.demo.domain.post.entity.PostEntity;
import com.example.demo.domain.post.mapper.PostMapper;
import com.example.demo.domain.post.presentation.data.request.CreatePostRequest;
import com.example.demo.domain.post.repository.PostBlockRepository;
import com.example.demo.domain.post.repository.PostRepository;
import com.example.demo.domain.post.service.CreatePostService;
import com.example.demo.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreatePostServiceImpl implements CreatePostService {

    private final PostRepository postRepository;
    private final MemberUtil memberUtil;
    private final PostBlockRepository postBlockRepository;
    private final PostMapper postMapper;

    @Override
    public void execute(CreatePostRequest request) {
        MemberEntity member = memberUtil.getCurrentMember();

        PostEntity post = postMapper.toPostEntity(member, request);
        postRepository.save(post);

        if (!request.contents().isEmpty()) {
            List<PostBlockEntity> blocks =
                    postMapper.toPostBlocks(request.contents(), post);
            postBlockRepository.saveAll(blocks);
        }
    }
}