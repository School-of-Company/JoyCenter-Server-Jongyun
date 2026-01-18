package com.example.demo.domain.attachments.repository;

import com.example.demo.domain.attachments.entity.AttachmentsEntity;
import com.example.demo.domain.attachments.entity.constant.AttachmentsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentsRepository extends JpaRepository<AttachmentsEntity, Long> , AttachmentsRepositoryCustom {
    List<AttachmentsEntity> findByPostId(Long postId);
    List<AttachmentsEntity> findAllByPostIdInAndAttachmentsTypeOrderByPostIdAscImageOrderAsc(List<Long> post, AttachmentsType attachmentsType);
}
