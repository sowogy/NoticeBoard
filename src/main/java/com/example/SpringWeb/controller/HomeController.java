package com.example.SpringWeb.controller;

import com.example.SpringWeb.dto.MemberForm;
import com.example.SpringWeb.dto.PasswordForm;
import com.example.SpringWeb.model.Member;
import com.example.SpringWeb.model.MemberUserDetails;
import com.example.SpringWeb.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;

    @GetMapping
    public String getHome(){
        return "forward:/article/list";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    // 카카오 로그인 진입점
    @GetMapping("/login/kakao")
    public String kakaoLogin() {
        String location = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + URLEncoder.encode(redirect_uri, StandardCharsets.UTF_8);

        return "redirect:" + location;
    }

    @GetMapping("/logout")
    public String getLogout(){
        return "logout";
    }

    @GetMapping("/signup")
    public String getMemberAdd(@ModelAttribute("member") MemberForm memberForm){
        return "signup";
    }

    @PostMapping("/signup")
    public String postMemberAdd(@Valid @ModelAttribute("member") MemberForm memberForm,
                                BindingResult bindingResult){
        if(memberForm.getPasswd() == null || memberForm.getPasswd().trim().length() < 8){
            bindingResult.rejectValue("password", "NotBlank", "패스워드를 8자 이상 입력하세요");
        }
        if(!memberForm.getPasswd().equals(memberForm.getPasswdConfirm())){
            bindingResult.rejectValue("passwordConfirm", "MissMatch", "비밀번호가 다릅니다");
        }
        if(memberService.findByEmail(memberForm.getEmail()).isPresent()){
            bindingResult.rejectValue("email", "AlreadyExist", "사용중인 이메일입니다");
        }
        if(bindingResult.hasErrors()){
            return "signup";
        }
        memberService.create(memberForm);
        return "redirect:/";
    }

    @PostMapping("/password")
    public String postPassword(@Valid @ModelAttribute("password") PasswordForm passwordForm,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal MemberUserDetails memberUserDetails){
        if(!memberService.checkPassword(
                memberUserDetails.getMemberId(), passwordForm.getOld()
        )){
            bindingResult.rejectValue("old", "MissMatch", "비밀번호가 잘못 됐습니다");
        }
        if(!passwordForm.getNew_password().equals(passwordForm.getNew_passwordConfirm())){
            bindingResult.rejectValue("new_passwordConfirm", "MissMatch", "바꿀 비밀번호와 확인 비밀번호가 틀립니다");
        }
        if(bindingResult.hasErrors()){
            return "/password";
        }
        memberService.updatePassword(memberUserDetails.getMemberId(), passwordForm.getNew_password());
        return "redirect:/";
    }

    @GetMapping("/password")
    public String getPassword(@ModelAttribute("password")PasswordForm passwordForm){
        return "password";
    }

    @GetMapping("/intro")
    public String getIntro(){
        return "intro";
    }
}
