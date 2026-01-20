package com.example.demo.global.util;

import com.example.demo.domain.member.entity.MemberEntity;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.global.auth.MemberDetails;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    private final MemberRepository memberRepository;

    public MemberEntity getCurrentMember() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof MemberDetails memberDetails) {
            Long memberId = memberDetails.getMember().getId();
            return memberRepository.findById(memberId)
                    .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
        }

        throw new GlobalException(ErrorCode.USER_NOT_FOUND);
    }
}
