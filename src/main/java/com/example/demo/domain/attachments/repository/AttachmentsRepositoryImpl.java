package com.example.demo.domain.attachments.repository;

import com.example.demo.domain.attachments.entity.AttachmentsEntity;
import com.example.demo.domain.attachments.entity.QAttachmentsEntity;
import com.example.demo.domain.attachments.entity.constant.AttachmentsType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class AttachmentsRepositoryImpl implements AttachmentsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AttachmentsEntity> findOrderedImages(List<Long> postId, AttachmentsType type) {

        QAttachmentsEntity attachmentsEntity = QAttachmentsEntity.attachmentsEntity;
        return queryFactory
                .selectFrom(attachmentsEntity)
                .where(attachmentsEntity.post.id.in(postId)
                        .and(attachmentsEntity.attachmentsType.eq(type)))
                .orderBy(
                        attachmentsEntity.post.id.asc(),
                        attachmentsEntity.imageOrder.asc())
                .fetch();
    }
}
