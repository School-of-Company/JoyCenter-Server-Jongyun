package com.example.demo.domain.post.entity;

import com.example.demo.domain.attachments.entity.AttachmentsEntity;
import com.example.demo.domain.post.entity.constant.BlockType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "post_block")
public class PostBlockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "block_order", nullable = false)
    private Integer blockOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "block_type", nullable = false)
    private BlockType blockType;

    @Column(name = "text", length = 1000)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachments_id")
    private AttachmentsEntity attachments;
}
