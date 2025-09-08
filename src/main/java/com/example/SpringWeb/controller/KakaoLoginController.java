package com.example.SpringWeb.controller;

import com.example.SpringWeb.config.SecurityConfig;
import com.example.SpringWeb.dto.KakaoUserInfoDTO;
import com.example.SpringWeb.model.MemberUserDetails;
import com.example.SpringWeb.repository.AuthorityRepository;
import com.example.SpringWeb.repository.MemberRepository;
import com.example.SpringWeb.service.KakaoService;
import com.example.SpringWeb.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RequiredArgsConstructor
public class KakaoLoginController {
    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final UserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository(); // 추가

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoDTO kakaoUserInfoDTO = kakaoService.getUserInfo(accessToken);

        String userEmail = kakaoUserInfoDTO.getKakaoAccount().getEmail();
        if (!memberService.isEmail(userEmail)) {
            return "redirect:/member/edit";
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // (선택) 요청 정보 디테일 부여
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // SecurityContext 생성 및 설정
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // ★ 명시적으로 세션/컨텍스트 저장 ★
        securityContextRepository.saveContext(context, request, response);

        return "redirect:/";
    }
}

