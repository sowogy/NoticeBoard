package com.example.SpringWeb.service;

import com.example.SpringWeb.dto.MemberDTO;
import com.example.SpringWeb.model.Article;
import com.example.SpringWeb.model.Member;
import com.example.SpringWeb.repository.ArticleRepository;
import com.example.SpringWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDTO findById(Long id) {
        return memberRepository.findById(id).map(this::mapToMemberDTO).orElseThrow();
    }

    private MemberDTO mapToMemberDTO(Member member){
        return MemberDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}
