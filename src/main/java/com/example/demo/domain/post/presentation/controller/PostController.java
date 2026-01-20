package com.example.demo.domain.post.presentation.controller;

import com.example.demo.domain.post.entity.constant.Sort;
import com.example.demo.domain.post.presentation.data.request.CreatePostRequest;
import com.example.demo.domain.post.presentation.data.request.UpdatePostRequest;
import com.example.demo.domain.post.presentation.data.response.PostListResponse;
import com.example.demo.domain.post.presentation.data.response.PostResponse;
import com.example.demo.domain.post.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final CreatePostService createPostService;
    private final DeletePostService deletePostService;
    private final UpdatePostService updatePostService;
    private final FindPostService findPostService;
    private final FindAllPostService findAllPostService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody CreatePostRequest createPostRequest) {
        createPostService.execute(createPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("postId") Long postId) {
        PostResponse response = findPostService.execute(postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<PostListResponse> getAllPosts(
            @RequestParam(defaultValue = "CREATED_AT_DESC") Sort sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PostListResponse response = findAllPostService.execute(sort,page,size);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId) {
        deletePostService.execute(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void>  updatePost(UpdatePostRequest request, @PathVariable("postId") Long postId) {
        updatePostService.execute(request,postId);
        return ResponseEntity.ok().build();
    }
}
