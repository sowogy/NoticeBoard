package com.example.SpringWeb.service;

import com.example.SpringWeb.dto.MemberDTO;
import com.example.SpringWeb.dto.MemberForm;
import com.example.SpringWeb.model.Article;
import com.example.SpringWeb.model.Member;
import com.example.SpringWeb.repository.ArticleRepository;
import com.example.SpringWeb.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkPassword(Long id, String password){
        Member member = memberRepository.findById(id).orElseThrow();
        return passwordEncoder.matches(password, member.getPasswd());
    }

    public void updatePassword(Long id, String password){
        Member member = memberRepository.findById(id).orElseThrow();
        member.setPasswd(passwordEncoder.encode(password));
        memberRepository.save(member);
    }
    public MemberDTO create(MemberForm memberForm){
        Member member = Member.builder()
                .name(memberForm.getName())
                .email(memberForm.getEmail())
                .passwd(passwordEncoder.encode(memberForm.getPasswd())) //Password는 암호화 후 저장
                .build();
        memberRepository.save(member);
        return mapToMemberDTO(member);
    }

    public boolean isEmail(String email){
        return memberRepository.findByEmail(email).isPresent();
    }

    public Optional<MemberDTO> findByEmail(String email){
        return memberRepository.findByEmail(email).map(this::mapToMemberDTO);
    }

    public MemberDTO findById(Long id) {
        return memberRepository.findById(id).map(this::mapToMemberDTO).orElseThrow();
    }

    public Page<MemberDTO> findAll(Pageable pageable){
        return memberRepository.findAll(pageable).map(this::mapToMemberDTO);
    }

    public MemberDTO patch(MemberForm memberForm){
        Member member = memberRepository.findById(memberForm.getId()).orElseThrow();

        if(memberForm.getName() != null){
            member.setName(memberForm.getName());
        }

        if(memberForm.getPasswd() != null){
            member.setPasswd(memberForm.getPasswd());
        }

        if(memberForm.getEmail() != null){
            member.setEmail(memberForm.getEmail());
        }
        memberRepository.save(member);
        return mapToMemberDTO(member);
    }

    @Transactional
    public void deleteById(Long id){
        Member member = memberRepository.findById(id).orElseThrow();
        articleRepository.deleteAllByMember(member);
        memberRepository.delete(member);
    }

    private MemberDTO mapToMemberDTO(Member member){
        return MemberDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}
