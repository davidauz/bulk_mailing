package com.davidauz.bulk_mailing.controller;

import com.davidauz.bulk_mailing.entity.Project;
import com.davidauz.bulk_mailing.repository.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class projectsController {

    @Autowired
    private ProjectsRepository projectsRepository;


    @GetMapping("projects")
    public String getAll
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ) {
        try {
            List<Project> projects_list = new ArrayList<Project>();
            Pageable paging = PageRequest.of(page - 1, pageSize);

            Page<Project> projects_page;
            if (keyword == null)
                projects_page = projectsRepository.findAll(paging);
            else {
                projects_page = projectsRepository.findByNameContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }

            projects_list = projects_page.getContent();

            model.addAttribute("projects", projects_list);
            model.addAttribute("currentPage", projects_page.getNumber() + 1);
            model.addAttribute("totalItems", projects_page.getTotalElements());
            model.addAttribute("totalPages", projects_page.getTotalPages());
            model.addAttribute("pageSize", pageSize);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "lists/projects";
    }

}
