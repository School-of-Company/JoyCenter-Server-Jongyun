package com.example.demo.domain.post.service;

import com.example.demo.domain.post.presentation.data.request.CreatePostRequest;

public interface CreatePostService {
    void execute(CreatePostRequest request);
}
