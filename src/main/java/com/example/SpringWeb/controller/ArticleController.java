package com.example.SpringWeb.controller;

import com.example.SpringWeb.dto.ArticleDTO;
import com.example.SpringWeb.dto.ArticleForm;
import com.example.SpringWeb.model.Article;
import com.example.SpringWeb.model.MemberUserDetails;
import com.example.SpringWeb.repository.ArticleRepository;
import com.example.SpringWeb.service.ArticleService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/content")
    public String getContent(@RequestParam("id") Long id, Model model){
        model.addAttribute("article", articleService.findById(id));
        return "article-content";
    }

    @GetMapping("/add")
    public String getAdd(@ModelAttribute("article") ArticleForm articleForm){
        articleForm.setDescription("욕설 사용 금지");
        return "article-add";
    }

    @PostMapping("/add")
    public String postAdd(@Valid @ModelAttribute("article") ArticleForm articleForm,
                          BindingResult bindingResult,
                          @AuthenticationPrincipal MemberUserDetails memberUserDetails){
        if(bindingResult != null && articleForm.getTitle().contains("tlqkf")){
            bindingResult.rejectValue("title", "SlangDetected", "욕설이 탐지되었습니다.");
        }
        if(bindingResult != null && articleForm.getDescription().contains("tlqkf")){
            bindingResult.rejectValue("description", "SlangDetected", "욕설이 탐지되었습니다.");
        }
        if(bindingResult.hasErrors()){
            return "article-add";
        }
        articleService.create(memberUserDetails.getMemberId(), articleForm);
        return "redirect:/article/list";
    }
}
