package com.example.demo.domain.attachments.presentation.controller;

import com.example.demo.domain.attachments.entity.constant.AttachmentsType;
import com.example.demo.domain.attachments.presentation.data.UploadAttachmentsResponse;
import com.example.demo.domain.attachments.service.DeleteAttachmentsService;
import com.example.demo.domain.attachments.service.UploadAttachmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attachments")
public class AttachmentsController {

    private final DeleteAttachmentsService deleteAttachmentsService;
    private final UploadAttachmentsService uploadAttachmentsService;

    @PostMapping
    public ResponseEntity<UploadAttachmentsResponse> uploadAttachments(
            @RequestPart("file") MultipartFile file,
            @RequestParam("attachmentsType") AttachmentsType attachmentsType,
            @RequestParam("imageOrder") Integer imageOrder
            ) throws IOException {
        return ResponseEntity.ok(uploadAttachmentsService.execute(file, attachmentsType, imageOrder));
    }
    @DeleteMapping("/{attachmentsId}")
    public ResponseEntity<Void> deleteAttachments(
            @PathVariable("attachmentsId") Long AttachmentsId) {
        deleteAttachmentsService.execute(AttachmentsId);
        return ResponseEntity.noContent().build();
    }
}
