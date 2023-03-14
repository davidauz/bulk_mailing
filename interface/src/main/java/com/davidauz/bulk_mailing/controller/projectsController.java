package com.davidauz.bulk_mailing.controller;

import com.davidauz.bulk_mailing.entity.*;
import com.davidauz.bulk_mailing.repository.*;
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

import java.util.*;

@Controller
public class projectsController {

    @Autowired
    private ProjectsRepository projectsRepository;
    @Autowired
    private PersonRepository personRepository;

    @Autowired private CompanyRepository companyRepository;

    @Autowired private GroupRepository groupRepository;

    @Autowired private PostRepository postRepository;

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
                projects_page = projectsRepository.findByMailSubjectContainingIgnoreCase(keyword, paging);
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
    public String project_new
    (   Model model
    ){
        Long id;
        String str;
        Map<Long, String> txt_id_titles = new HashMap<>();

        model.addAttribute("all_persons", personRepository.findAll());
        model.addAttribute("all_companies", companyRepository.findAll());
        model.addAttribute("all_groups", groupRepository.findAll());
        for(IdTitle idt : postRepository.findAllBy())
            txt_id_titles.put(idt.getId(), idt.getTitle());
        model.addAttribute("all_texts", txt_id_titles);
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
            case "rm_groups":
                return rm_groups((ArrayList<String>) requestData.get("groups"));
            case "rm_companies":
                return rm_companies((ArrayList<String>) requestData.get("ajx_data"));
            case "add_companies":
                return add_companies((ArrayList<String>) requestData.get("ajx_data"));
        }
        return ResponseEntity.ok("success");
    }

    private ResponseEntity<String> add_groups(ArrayList<String> groups) {
// Retrieve groups whose ID is in list
        Optional<Group> gr_o;
        Group gr;
        ArrayList<String[]> groupsToAdd=new ArrayList<String[]>()
        ,    peopleToAdd=new ArrayList<String[]>()
        ;
        for (String s_ID : groups) {
            gr_o=groupRepository.findById(Long.valueOf(s_ID));
            if(gr_o.isPresent()){
                gr=gr_o.get();
                groupsToAdd.add(new String[]{String.valueOf(gr.getGroupId()),gr.getGroupName()});
                for (Person o_pers : gr.getPeople()) {
// Retrieve all people belonging to said group
                    peopleToAdd.add(new String[]{String.valueOf(o_pers.getPersonId()), o_pers.getFirstName(),o_pers.getFamilyName()});
                }
            }
        }
        HashMap<String,Object> ret_data=new HashMap<>();
        ret_data.put("GROUPS",groupsToAdd);
        ret_data.put("PEOPLE",peopleToAdd);
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



    private ResponseEntity<String> rm_groups(ArrayList<String> groups) {
// Retrieve groups whose ID is in list
        Optional<Group> gr_o;
        Group gr;
        ArrayList<String[]> groupsToRemove=new ArrayList<String[]>()
        ,    peopleToRemove=new ArrayList<String[]>()
        ;
        for (String s_ID : groups) {
            gr_o=groupRepository.findById(Long.valueOf(s_ID));
            if(gr_o.isPresent()){
                gr=gr_o.get();
                groupsToRemove.add(new String[]{String.valueOf(gr.getGroupId()),gr.getGroupName()});
                for (Person o_pers : gr.getPeople()) {
// Retrieve all people belonging to said group
                    peopleToRemove.add(new String[]{String.valueOf(o_pers.getPersonId()), o_pers.getFirstName(),o_pers.getFamilyName()});
                }
            }
        }
        HashMap<String,Object> ret_data=new HashMap<>();
        ret_data.put("GROUPS",groupsToRemove);
        ret_data.put("PEOPLE",peopleToRemove);
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






    private ResponseEntity<String> add_companies(ArrayList<String> companies) {
// Retrieve groups whose ID is in list
        Optional<Company> comp_o;
        Company comp;
        ArrayList<String[]> companiesToAdd=new ArrayList<String[]>()
                ,    peopleToAdd=new ArrayList<String[]>()
                ;
        for (String s_ID : companies) {
            comp_o=companyRepository.findById(Long.valueOf(s_ID));
            if(comp_o.isPresent()){
                comp=comp_o.get();
                companiesToAdd.add(new String[]{String.valueOf(comp.getId()),comp.getName()});
                List<Person> ppl=personRepository.findByCompany_Id(comp.getId());
                for (Person o_pers : ppl) {
// Retrieve all people belonging to said group
                    peopleToAdd.add(new String[]{String.valueOf(o_pers.getPersonId()), o_pers.getFirstName(),o_pers.getFamilyName()});
                }
            }
        }
        HashMap<String,Object> ret_data=new HashMap<>();
        ret_data.put("COMPANIES",companiesToAdd);
        ret_data.put("PEOPLE",peopleToAdd);
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




    private ResponseEntity<String> rm_companies(ArrayList<String> companies) {
// Retrieve groups whose ID is in list
        Optional<Company> comp_o;
        Company comp;
        ArrayList<String[]> companiesToAdd=new ArrayList<String[]>()
                ,    peopleToAdd=new ArrayList<String[]>()
                ;
        for (String s_ID : companies) {
            comp_o=companyRepository.findById(Long.valueOf(s_ID));
            if(comp_o.isPresent()){
                comp=comp_o.get();
                companiesToAdd.add(new String[]{String.valueOf(comp.getId()),comp.getName()});
                List<Person> ppl=personRepository.findByCompany_Id(comp.getId());
                for (Person o_pers : ppl) {
// Retrieve all people belonging to said group
                    peopleToAdd.add(new String[]{String.valueOf(o_pers.getPersonId()), o_pers.getFirstName(),o_pers.getFamilyName()});
                }
            }
        }
        HashMap<String,Object> ret_data=new HashMap<>();
        ret_data.put("COMPANIES",companiesToAdd);
        ret_data.put("PEOPLE",peopleToAdd);
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






    @GetMapping("/projects/edit/{projectId}")
    public String project_edit
    (   Model model
    ,   @PathVariable Long projectId
    ){
        Project comp = projectsRepository.findById(projectId).get();
        Map<Long, String> txt_id_titles = new HashMap<>();
        List<Person> lp = personRepository.findAll();
        for(Person p : comp.getPeople())
            lp.remove(p);
        model.addAttribute("all_persons", lp);
        model.addAttribute("all_companies", companyRepository.findAll());
        model.addAttribute("all_groups", groupRepository.findAll());
        for(IdTitle idt : postRepository.findAllBy())
            txt_id_titles.put(idt.getId(), idt.getTitle());
        model.addAttribute("all_texts", txt_id_titles);
        model.addAttribute("project", comp);
        return "forms/project_form";
    }



    @PostMapping("/project/insert")
    public String project_insert
    (   Project pro
    ,   Model model
    ) {
        projectsRepository.save(pro);
        return project_edit(model, pro.getId());
    }



    @PostMapping("/projects/save_new")
    public String projectSaveAsNew
    (   Project pro
            ,   Model model
    ) {
        pro.setId(null);
        projectsRepository.save(pro);
        return project_edit(model, pro.getId());
    }




}

