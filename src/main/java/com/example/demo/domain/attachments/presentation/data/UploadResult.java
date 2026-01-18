package com.example.demo.domain.attachments.presentation.data;

public record UploadResult(
        String s3Key,
        String url
) {

}
