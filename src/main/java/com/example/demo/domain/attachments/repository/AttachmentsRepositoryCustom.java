package com.example.demo.domain.attachments.repository;

import com.example.demo.domain.attachments.entity.AttachmentsEntity;
import com.example.demo.domain.attachments.entity.constant.AttachmentsType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentsRepositoryCustom {
    List<AttachmentsEntity> findOrderedImages(List<Long> postIds, AttachmentsType type);
}
