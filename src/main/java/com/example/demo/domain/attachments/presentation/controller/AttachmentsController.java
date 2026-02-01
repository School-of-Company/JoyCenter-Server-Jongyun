package com.example.demo.domain.attachments.presentation.controller;

import com.example.demo.domain.attachments.entity.constant.AttachmentsType;
import com.example.demo.domain.attachments.presentation.data.UploadAttachmentsResponse;
import com.example.demo.domain.attachments.service.DeleteAttachmentsService;
import com.example.demo.domain.attachments.service.UploadAttachmentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "첨부파일", description = "첨부파일 업로드 및 삭제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attachments")
public class AttachmentsController {

    private final DeleteAttachmentsService deleteAttachmentsService;
    private final UploadAttachmentsService uploadAttachmentsService;

    @Operation(
            summary = "첨부파일 업로드",
            description = "이미지 또는 파일을 업로드합니다. multipart/form-data 형식으로 전송해야 합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "파일 업로드 성공",
                    content = @Content(schema = @Schema(implementation = UploadAttachmentsResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 파일 형식 또는 요청"),
            @ApiResponse(responseCode = "413", description = "파일 크기 초과")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadAttachmentsResponse> uploadAttachments(
            @Parameter(description = "업로드할 파일", required = true)
            @RequestPart("file") MultipartFile file,
            @Parameter(description = "첨부파일 타입 (예: IMAGE, DOCUMENT 등)", required = true)
            @RequestParam("attachmentsType") AttachmentsType attachmentsType,
            @Parameter(description = "이미지 순서 (정렬용)", required = true)
            @RequestParam("imageOrder") Integer imageOrder
    ) throws IOException {
        return ResponseEntity.ok(uploadAttachmentsService.execute(file, attachmentsType, imageOrder));
    }

    @Operation(summary = "첨부파일 삭제", description = "첨부파일 ID로 특정 첨부파일을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "파일 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "첨부파일을 찾을 수 없음")
    })
    @DeleteMapping("/{attachmentsId}")
    public ResponseEntity<Void> deleteAttachments(
            @Parameter(description = "첨부파일 ID", required = true)
            @PathVariable("attachmentsId") Long attachmentsId) {
        deleteAttachmentsService.execute(attachmentsId);
        return ResponseEntity.noContent().build();
    }
}