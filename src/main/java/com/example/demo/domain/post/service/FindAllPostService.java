package com.example.demo.domain.post.service;

import com.example.demo.domain.post.entity.constant.Sort;
import com.example.demo.domain.post.presentation.data.response.PostListResponse;
public interface FindAllPostService {
    PostListResponse execute(Sort sort, int page, int size);
}
