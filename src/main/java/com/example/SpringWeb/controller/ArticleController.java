package com.example.SpringWeb.controller;

import com.example.SpringWeb.dto.ArticleDTO;
import com.example.SpringWeb.repository.ArticleRepository;
import com.example.SpringWeb.service.ArticleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/list")
    public String getList(@PageableDefault(size = 10, sort="id", direction = Sort.Direction.DESC)
                          Pageable pageable, Model model){
        Page<ArticleDTO> page = articleService.findAll(pageable);
        model.addAttribute("page", page);
        return "article-list";
    }
}
