package com.example.SpringWeb.repository;

import com.example.SpringWeb.model.Article;
import com.example.SpringWeb.model.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Transactional
    void deleteAllByMember(Member member);
}
