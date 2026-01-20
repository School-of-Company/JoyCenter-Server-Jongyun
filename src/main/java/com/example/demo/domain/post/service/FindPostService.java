package com.example.demo.domain.post.service;

import com.example.demo.domain.post.presentation.data.response.PostResponse;

public interface FindPostService {
    PostResponse execute(Long postId);
}