package com.davidauz.blkm_interface.controller;

import com.davidauz.blkm_interface.entity.Group;
import com.davidauz.blkm_interface.entity.Person;
import com.davidauz.blkm_interface.repository.GroupRepository;
import com.davidauz.blkm_interface.repository.PersonRepository;
import com.davidauz.blkm_interface.entity.Person;
import com.davidauz.blkm_interface.repository.GroupRepository;
import com.davidauz.blkm_interface.repository.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GroupsController {

    @Autowired
    private PersonRepository personRepository;




    @Autowired
    private GroupRepository groupRepository;


    @PostMapping("/groups/search")
    public String groups_search
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ){
        return getAll(model, keyword,page,pageSize);
    }

    @PostMapping("/groups/page/{pageNum}")
    public String companies_page
    (   Model model
            ,   @RequestParam(required = false) String keyword
            ,   @RequestParam(defaultValue = "1") int currentPage
            ,   @RequestParam(defaultValue = "30") int pageSize
            ,   @RequestParam(defaultValue = "0") int totalPages
            ,   @PathVariable String pageNum
    ){
        return groups_search(model, keyword,Integer.valueOf(pageNum),pageSize);
    }

    @PostMapping("/groups/navigate/{direction}")
    public String companies_search_direction
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
        return groups_search(model, keyword,currentPage,pageSize);
    }


    @GetMapping("/groups")
    public String getAll
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ) {
        try {
            List<Group> groups;
            Pageable paging = PageRequest.of(page - 1, pageSize);

            Page<Group> groups_page;
            if (keyword == null)
                groups_page = groupRepository.findAll(paging);
            else {
                groups_page = groupRepository.findByGroupNameContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }

            groups = groups_page.getContent();

            model.addAttribute("groups", groups);
            model.addAttribute("currentPage", groups_page.getNumber() + 1);
            model.addAttribute("totalItems", groups_page.getTotalElements());
            model.addAttribute("totalPages", groups_page.getTotalPages());
            model.addAttribute("pageSize", pageSize);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "lists/groups";
    }





    @PostMapping("/groups/insert")
    public String insert_group
    (   Model model
    ,   @Valid @ModelAttribute Group group
    ,   BindingResult bindingResult
    ) {
        String message="";
        if (bindingResult.hasErrors()) {

            model.addAttribute("groups", groupRepository.findAll());
            return "forms/group_form";
        }
        groupRepository.save(group);
        return "redirect:/groups";
    }

    @GetMapping("/groups/edit/{groupId}")
    public String group_edit
    (   Model model
    ,   @PathVariable Long groupId
    ){
        Group group = groupRepository.findById(groupId).get();




        List<Person> l_available_persons= personRepository.findAll();
        for(Person pers: group.getPeople())
            if(l_available_persons.contains(pers))
                l_available_persons.remove(pers);
        model.addAttribute("group", group);
   
        model.addAttribute("available_persons", l_available_persons);
        model.addAttribute("member_of", group.getPeople());
        return "forms/group_form";
    }


    @GetMapping("/groups/delete/{groupId}")
    public String group_delete
    (   Model model
    ,   @PathVariable Long groupId
    ){
        groupRepository.deleteById(groupId);
        return "groups";
    }



    @GetMapping(value = "groups/new")
    public String group_new
    (   Model model
    ){
        Group group = new Group();

        model.addAttribute("available_persons", personRepository.findAll());

        model.addAttribute("group", group);
        return "forms/group_form";
    }


    @GetMapping("/groups/{groupId}/published/{status}")
    public String group_set_published
    (   Model model
    ,   @PathVariable Long groupId
    ,   @PathVariable("status") boolean published
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ){
        Group group = null;
        try {
            group = groupRepository.findById(groupId).orElseThrow(() -> new Exception("`" + groupId + "`: no such Group ID"));
            group.setActive(published);
            groupRepository.save(group);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return getAll(model, keyword, page, pageSize);
    }










}
