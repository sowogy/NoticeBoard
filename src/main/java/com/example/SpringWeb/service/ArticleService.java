package com.example.SpringWeb.service;

import com.example.SpringWeb.dto.ArticleDTO;
import com.example.SpringWeb.dto.ArticleForm;
import com.example.SpringWeb.model.Article;
import com.example.SpringWeb.model.Member;
import com.example.SpringWeb.repository.ArticleRepository;
import com.example.SpringWeb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

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

    public ArticleDTO create(Long memberId, ArticleForm articleForm){
        Member member = memberRepository.findById(memberId).orElseThrow();
        Article article = Article.builder()
                .title(articleForm.getTitle())
                .description(articleForm.getDescription())
                .member(member)
                .build();
        articleRepository.save(article);
        return mapTOArticleDTO(article);
    }

    public Page<ArticleDTO> findAll(Pageable pageable){
        return articleRepository.findAll(pageable).map(this::mapTOArticleDTO);
    }

    public ArticleDTO findById(Long id){
        return articleRepository.findById(id).map(this::mapTOArticleDTO).orElseThrow();
    }

    public ArticleDTO update(ArticleForm articleForm){
        Article article = articleRepository.findById(articleForm.getId()).orElseThrow();
        article.setTitle(articleForm.getTitle());
        article.setDescription(article.getDescription());
        articleRepository.save(article);
        return mapTOArticleDTO(article);
    }

    public void delete(Long id){
        Article article = articleRepository.findById(id).orElseThrow();
        articleRepository.delete(article);
    }
}
