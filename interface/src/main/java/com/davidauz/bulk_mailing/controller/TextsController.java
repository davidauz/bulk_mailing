package com.davidauz.bulk_mailing.controller;

import com.davidauz.bulk_mailing.entity.Post;
import com.davidauz.bulk_mailing.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class TextsController {

    @Autowired
    private PostRepository postrepository;

    @GetMapping("/texts")
    public String get_texts
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ){
        List<Post> list_of_posts;
        Pageable paging = PageRequest.of(page - 1, pageSize);
        Page<Post> posts_page;
        if (keyword == null)
            posts_page = postrepository.findAll(paging);
        else {
            posts_page = postrepository.findByTitleContainingIgnoreCase(keyword, paging);
            model.addAttribute("keyword", keyword);
        }
        list_of_posts=posts_page.getContent();
        model.addAttribute("posts", list_of_posts);
        model.addAttribute("currentPage", posts_page.getNumber() + 1);
        model.addAttribute("totalItems", posts_page.getTotalElements());
        model.addAttribute("totalPages", posts_page.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        return "lists/posts";
    }

    @GetMapping("/texts/new")
    public String main
    (   Model model
    ) {
        model.addAttribute("post", new Post());
        return "forms/text_form";
    }


    @PostMapping(path="/texts/preview", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody // returning raw content and not a template name
    public String preview
    (   Post post
    ,   Model model
    ) {
        model.addAttribute("post", post);
        return post.getContent();
    }

    @PostMapping("/texts/search")
    public String texts_search
    (   Model model
            ,   @RequestParam(required = false) String keyword
            ,   @RequestParam(defaultValue = "1") int currentPage
            ,   @RequestParam(defaultValue = "30") int pageSize
    ){
        return get_texts(model, keyword,currentPage,pageSize);
    }

    @PostMapping("/texts/navigate/{direction}")
    public String texts_search_direction
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int currentPage
    ,   @RequestParam(defaultValue = "30") int pageSize
    ,   @RequestParam(defaultValue = "0") int totalPages
    ,   @PathVariable String direction
    ){
        if(direction.equals("next"))
            currentPage+=1;
        else if(direction.equals("prev"))
            currentPage-=1;
        else if(direction.equals("first"))
            currentPage=1;
        else if(direction.equals("last"))
            currentPage=totalPages;
        return texts_search(model, keyword,currentPage,pageSize);
    }

    @PostMapping("/texts/page/{pageNum}")
    public String companies_page
    (   Model model
            ,   @RequestParam(required = false) String keyword
            ,   @RequestParam(defaultValue = "1") int currentPage
            ,   @RequestParam(defaultValue = "30") int pageSize
            ,   @RequestParam(defaultValue = "0") int totalPages
            ,   @PathVariable String pageNum
    ){
        return texts_search(model, keyword,Integer.valueOf(pageNum),pageSize);
    }


    @PostMapping("/texts/submit")
    public String save
    (   Post post
    ,   Model model
    ) {
        postrepository.save(post);
        model.addAttribute("post", post);
        model.addAttribute("message", "Text was saved");
        return "forms/text_form";
    }

    @GetMapping("/texts/edit/{textId}")
    public String person_edit
    (   Model model
    ,   @PathVariable Long textId
    ){
        Post post = postrepository.findById(textId).get();
        model.addAttribute("post", post);
        return "forms/text_form";
    }

}