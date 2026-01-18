package com.example.demo.global.s3.service;

import com.example.demo.domain.attachments.exception.AttachmentsDeleteFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteService {

    private final S3AsyncClient s3AsyncClient;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;


    @Async
    public CompletableFuture<Void> execute(String s3Key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .build();

        return s3AsyncClient.deleteObject(request)
                .thenAccept(response ->
                        log.info("S3 객체 삭제 완료: key={}", s3Key)
                )
                .exceptionally(ex -> {
                    log.error("S3 객체 삭제 실패: key={}", s3Key, ex);
                    throw new AttachmentsDeleteFailedException();
                });
    }
}
