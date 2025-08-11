package com.example.SpringWeb.service;

import com.example.SpringWeb.dto.ArticleDTO;
import com.example.SpringWeb.model.Article;
import com.example.SpringWeb.model.Member;
import com.example.SpringWeb.repository.ArticleRepository;
import com.example.SpringWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    private ArticleDTO mapTOArticleDTO(Article article){
        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .created(article.getCreated())
                .updated(article.getUpdated())
                .member_id(article.getMember().getId())
                .name(article.getMember().getName())
                .email(article.getMember().getEmail())
                .build();
    }
}
