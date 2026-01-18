package com.example.demo.domain.attachments.service.impl;

import com.example.demo.domain.attachments.entity.AttachmentsEntity;
import com.example.demo.domain.attachments.entity.constant.AttachmentsType;
import com.example.demo.domain.attachments.presentation.data.UploadAttachmentsResponse;
import com.example.demo.domain.attachments.presentation.data.UploadResult;
import com.example.demo.domain.attachments.repository.AttachmentsRepository;
import com.example.demo.domain.attachments.service.UploadAttachmentsService;
import com.example.demo.global.s3.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UploadAttachmentsServiceImpl implements UploadAttachmentsService {

    private final UploadService uploadService;
    private final AttachmentsRepository attachmentsRepository;

    @Override
    public UploadAttachmentsResponse execute(MultipartFile file, AttachmentsType type, Integer imageOrder) throws IOException {
        UploadResult response = uploadService.execute(
                file.getOriginalFilename(),
                file.getInputStream(),
                type
        ).join();

        AttachmentsEntity attachmentsEntity = AttachmentsEntity.builder()
                .s3Key(response.s3Key())
                .attachmentsUrl(response.url())
                .attachmentsType(type)
                .imageOrder(imageOrder)
                .build();

        attachmentsRepository.save(attachmentsEntity);

        return new UploadAttachmentsResponse(
                attachmentsEntity.getId(),
                attachmentsEntity.getAttachmentsUrl(),
                attachmentsEntity.getAttachmentsType());
    }

    @Override
    public List<UploadAttachmentsResponse> executeMultiple(List<MultipartFile> files, AttachmentsType type) {
        List<CompletableFuture<AttachmentsEntity>> futures = IntStream.range(0, files.size())
                .mapToObj(index -> {
                    MultipartFile file = files.get(index);
                    return uploadService.execute(
                            file.getOriginalFilename(),
                            createInputStream(file),
                            type
                    ).thenApply(result -> AttachmentsEntity.builder()
                            .s3Key(result.s3Key())
                            .attachmentsUrl(result.url())
                            .attachmentsType(type)
                            .imageOrder(index)

                            .build());

                }).toList();

        List<AttachmentsEntity> entities = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        attachmentsRepository.saveAll(entities);

        return entities.stream()
                .map(entity -> new UploadAttachmentsResponse(
                        entity.getId(),
                        entity.getAttachmentsUrl(),
                        entity.getAttachmentsType()))
                .toList();
    }

    private InputStream createInputStream(MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패: " + file.getOriginalFilename(), e);
        }
    }
}