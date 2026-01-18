package com.example.demo.global.s3.service;

import com.example.demo.domain.attachments.entity.constant.AttachmentsType;
import com.example.demo.domain.attachments.exception.AttachmentsUploadFailedException;
import com.example.demo.domain.attachments.presentation.data.UploadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadService {

    private final S3AsyncClient s3AsyncClient;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    private static final long MAX_SIZE = 100 * 1024 * 1024;

    private static final Map<AttachmentsType, List<String>> ALLOWED_EXTS = Map.of(
            AttachmentsType.IMAGE, List.of("jpg", "jpeg", "png", "gif", "webp"),
            AttachmentsType.VIDEO, List.of("mp4", "mov", "avi", "mkv"),
            AttachmentsType.FILE, List.of("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx")
    );

    @Async
    public CompletableFuture<UploadResult> execute(
            String fileName,
            InputStream inputStream,
            AttachmentsType type
    ) {
        try {
            if (fileName == null || !fileName.contains(".")) {
                throw new IllegalArgumentException();
            }

            String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            if (!ALLOWED_EXTS.get(type).contains(ext)) {
                throw new IllegalArgumentException();
            }

            byte[] data = inputStream.readAllBytes();
            if (data.length > MAX_SIZE) {
                throw new IllegalArgumentException();
            }

            String sanitizedName = fileName.replaceAll("[^a-zA-Z0-9가-힣._-]", "_");
            String key = type.getPath() + "/" + UUID.randomUUID() + "/" + sanitizedName;

            return s3AsyncClient.putObject(
                            PutObjectRequest.builder()
                                    .bucket(bucket)
                                    .key(key)
                                    .contentLength((long) data.length)
                                    .build(),
                            AsyncRequestBody.fromBytes(data)
                    )
                    .handle((resp, ex) -> {
                        if (ex != null) {
                            log.error("S3 업로드 중 에러 발생: ", ex); // 실제 에러 로그 출력
                            throw new AttachmentsUploadFailedException();
                        }
                        return new UploadResult(
                                key,
                                "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key
                        );
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
