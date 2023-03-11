package com.davidauz.bulk_mailing.controller;

import com.davidauz.bulk_mailing.entity.Project;
import com.davidauz.bulk_mailing.repository.CompanyRepository;
import com.davidauz.bulk_mailing.repository.GroupRepository;
import com.davidauz.bulk_mailing.repository.PersonRepository;
import com.davidauz.bulk_mailing.repository.ProjectsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class projectsController {

    @Autowired
    private ProjectsRepository projectsRepository;
    @Autowired
    private PersonRepository personRepository;

    @Autowired private CompanyRepository companyRepository;

    @Autowired private GroupRepository groupRepository;


    @GetMapping("/projects")
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


    @GetMapping(value = "/project_new")
    public String company_new
    (   Model model
    ){
        model.addAttribute("all_persons", personRepository.findAll());
        model.addAttribute("all_companies", companyRepository.findAll());
        model.addAttribute("all_groups", groupRepository.findAll());
        model.addAttribute("project", new Project());
        return "forms/project_form";
    }



    @PostMapping("/project/ajx_dispatcher")
    public ResponseEntity<String> handleAjxRequest
    (   @RequestBody Map<String, Object> requestData
    ) {
        String str_verb= (String) requestData.get("verb");
        if (str_verb == null)
            return ResponseEntity.badRequest().body("VERB parameter is missing");
        switch(str_verb){
            case "add_groups":
                return add_groups((ArrayList<String>) requestData.get("groups"));
        }
        return ResponseEntity.ok("success");
    }

    private ResponseEntity<String> add_groups(ArrayList<String> groups) {
        String[] ret_data=new String[] {"a", ":", "c"};
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(ret_data);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.OK);
    }

//Controllare nel POM.xml se servono veramente tutte quelle dependencysu JSON

}
