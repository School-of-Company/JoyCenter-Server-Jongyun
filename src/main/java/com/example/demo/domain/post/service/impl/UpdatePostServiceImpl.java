package com.example.demo.domain.post.service.impl;

import com.example.demo.domain.attachments.exception.NotFoundAttachmentException;
import com.example.demo.domain.member.entity.MemberEntity;
import com.example.demo.domain.post.entity.PostBlockEntity;
import com.example.demo.domain.post.entity.PostEntity;
import com.example.demo.domain.post.mapper.PostBlockMapper;
import com.example.demo.domain.post.presentation.data.request.UpdatePostRequest;
import com.example.demo.domain.post.repository.PostBlockRepository;
import com.example.demo.domain.post.repository.PostRepository;
import com.example.demo.domain.post.service.UpdatePostService;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.GlobalException;
import com.example.demo.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdatePostServiceImpl implements UpdatePostService {

    private final PostRepository postRepository;
    private final PostBlockRepository postBlockRepository;
    private final PostBlockMapper postBlockMapper;
    private final MemberUtil memberUtil;

    @Override
    public void execute(UpdatePostRequest request,Long postId) {
        MemberEntity member = memberUtil.getCurrentMember();

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(NotFoundAttachmentException::new);

        validateAuthor(post, member);

        if (request.title() != null) {
            post.update(request.title());
        }
        if (request.contents() != null) {
            postBlockRepository.deleteAllByPost(post);

            if (!request.contents().isEmpty()) {
                List<PostBlockEntity> blocks =
                        postBlockMapper.toEntities(request.contents(), post);
                postBlockRepository.saveAll(blocks);
            }
        }
    }

    private void validateAuthor(PostEntity post, MemberEntity member) {
        if (!post.getAuthor().getId().equals(member.getId())) {
            throw new GlobalException(ErrorCode.USER_UNAUTHORIZED);
        }
    }
}
