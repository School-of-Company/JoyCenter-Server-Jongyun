package com.example.demo.domain.post.presentation.controller;

import com.example.demo.domain.post.entity.constant.Sort;
import com.example.demo.domain.post.presentation.data.request.CreatePostRequest;
import com.example.demo.domain.post.presentation.data.request.UpdatePostRequest;
import com.example.demo.domain.post.presentation.data.response.PostListResponse;
import com.example.demo.domain.post.presentation.data.response.PostResponse;
import com.example.demo.domain.post.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시글", description = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final CreatePostService createPostService;
    private final DeletePostService deletePostService;
    private final UpdatePostService updatePostService;
    private final FindPostService findPostService;
    private final FindAllPostService findAllPostService;

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody CreatePostRequest createPostRequest) {
        createPostService.execute(createPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "게시글 조회", description = "게시글 ID로 특정 게시글을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("{postId}")
    public ResponseEntity<PostResponse> getPost(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable("postId") Long postId) {
        PostResponse response = findPostService.execute(postId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 목록 조회", description = "모든 게시글을 페이지네이션과 정렬 옵션으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
    })
    @GetMapping("/all")
    public ResponseEntity<PostListResponse> getAllPosts(
            @Parameter(description = "정렬 기준 (CREATED_AT_DESC, CREATED_AT_ASC 등)")
            @RequestParam(defaultValue = "CREATED_AT_DESC") Sort sort,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size
    ) {
        PostListResponse response = findAllPostService.execute(sort, page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 삭제", description = "게시글 ID로 특정 게시글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable("postId") Long postId) {
        deletePostService.execute(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "게시글 수정", description = "게시글 ID로 특정 게시글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @RequestBody UpdatePostRequest request,
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable("postId") Long postId) {
        updatePostService.execute(request, postId);
        return ResponseEntity.ok().build();
    }
}