package com.example.demo.domain.attachments.service.impl;

import com.example.demo.domain.attachments.entity.AttachmentsEntity;
import com.example.demo.domain.attachments.exception.NotFoundAttachmentException;
import com.example.demo.domain.attachments.repository.AttachmentsRepository;
import com.example.demo.domain.attachments.service.DeleteAttachmentsService;
import com.example.demo.global.s3.service.DeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAttachmentsServiceImpl implements DeleteAttachmentsService {

    private final AttachmentsRepository attachmentsRepository;
    private final DeleteService deleteService;
    @Override
    public void execute(Long attachmentId) {
        AttachmentsEntity attachmentsEntity = attachmentsRepository.findById(attachmentId)
                .orElseThrow(NotFoundAttachmentException::new);
        deleteService.execute(attachmentsEntity.getS3Key());
        attachmentsRepository.delete(attachmentsEntity);
    }
}
