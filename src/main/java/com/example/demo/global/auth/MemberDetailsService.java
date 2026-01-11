package com.example.demo.global.auth;

import com.example.demo.domain.member.exception.NotFoundMemberException;
import com.example.demo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(MemberDetails::new)
                .orElseThrow(NotFoundMemberException::new);
    }
}
