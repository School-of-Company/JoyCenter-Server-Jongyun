package com.example.demo.domain.attachments.entity;

import com.example.demo.domain.attachments.entity.constant.AttachmentsType;
import com.example.demo.domain.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "attachments")
@Builder
public class AttachmentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachments_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private PostEntity post;

    @Column(name = "attachments_url",  nullable = false)
    private String attachmentsUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "attachments_type")
    private AttachmentsType attachmentsType;

    @Column(name = "image_order")
    private Integer imageOrder;

    @Column(name = "`s3key`", nullable = false)
    private String s3Key;
}
