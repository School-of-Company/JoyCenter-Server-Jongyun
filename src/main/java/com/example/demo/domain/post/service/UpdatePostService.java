package com.example.demo.domain.post.service;

import com.example.demo.domain.post.presentation.data.request.UpdatePostRequest;

public interface UpdatePostService {
    void execute(UpdatePostRequest request,Long postId);
}
