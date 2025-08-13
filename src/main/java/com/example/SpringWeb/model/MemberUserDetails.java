package com.example.SpringWeb.model;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
public class MemberUserDetails implements UserDetails {
    private String name;
    private String passwd;
    private List<SimpleGrantedAuthority> authorities;

    //Extra
    private String displayName;
    private Long memberId;

    public MemberUserDetails(Member member, List<Authority> authorities){
        this.name = member.getEmail();
        this.displayName = member.getName();
        this.passwd = member.getPasswd();
        this.memberId = member.getId();
        this.authorities = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();
    }

    @Override
    public String getPassword() {
        return passwd;
    }

    @Override
    public String getUsername() {
        return displayName;
    }
}
