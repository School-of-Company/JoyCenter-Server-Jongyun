package com.example.demo.domain.attachments.service;

import com.example.demo.domain.attachments.entity.constant.AttachmentsType;
import com.example.demo.domain.attachments.presentation.data.UploadAttachmentsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UploadAttachmentsService {
    UploadAttachmentsResponse execute(MultipartFile file, AttachmentsType type , Integer imageOrder) throws IOException;
    List<UploadAttachmentsResponse> executeMultiple(List<MultipartFile> files, AttachmentsType type);
}
