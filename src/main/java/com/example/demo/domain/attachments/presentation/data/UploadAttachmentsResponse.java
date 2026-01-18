package com.example.demo.domain.attachments.presentation.data;

import com.example.demo.domain.attachments.entity.constant.AttachmentsType;

public record UploadAttachmentsResponse(
        Long attachmentsId,
        String url,
        AttachmentsType attachmentsType
) {}
