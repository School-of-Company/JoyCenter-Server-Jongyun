package com.example.demo.domain.post.service.impl;

import com.example.demo.domain.attachments.exception.NotFoundAttachmentException;
import com.example.demo.domain.post.entity.PostEntity;
import com.example.demo.domain.post.repository.PostBlockRepository;
import com.example.demo.domain.post.repository.PostRepository;
import com.example.demo.domain.post.service.DeletePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeletePostServiceImpl implements DeletePostService {

    private final PostRepository postRepository;

    @Override
    public void execute(Long postId) {

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(NotFoundAttachmentException::new);
        postRepository.delete(post);
    }
}
