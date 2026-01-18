package com.example.demo.domain.member.entity;

import com.example.demo.global.oauth.common.OAuthType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

}
