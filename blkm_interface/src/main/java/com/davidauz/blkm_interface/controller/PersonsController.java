package com.davidauz.blkm_interface.controller;

import com.davidauz.blkm_common.entity.Company;
import com.davidauz.blkm_common.entity.Group;
import com.davidauz.blkm_common.entity.Person;
import com.davidauz.blkm_common.repo.CompanyRepository;
import com.davidauz.blkm_common.repo.GroupRepository;
import com.davidauz.blkm_common.repo.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PersonsController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private GroupRepository groupRepo;


    @PostMapping("/people/page/{pageNum}")
    public String companies_page
    (   Model model
            ,   @RequestParam(required = false) String keyword
            ,   @RequestParam(defaultValue = "1") int currentPage
            ,   @RequestParam(defaultValue = "30") int pageSize
            ,   @RequestParam(defaultValue = "0") int totalPages
            ,   @PathVariable String pageNum
    ){
        return people_search(model, keyword,Integer.valueOf(pageNum),pageSize);
    }

    @PostMapping("/people/navigate/{direction}")
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
        return people_search(model, keyword,currentPage,pageSize);
    }

    @PostMapping("/people/search")
    public String people_search
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ){
        return getAll(model, keyword,page,pageSize);
    }


    @GetMapping("/people")
    public String getAll
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ) {
        try {
            List<Person> people;
            Pageable paging = PageRequest.of(page - 1, pageSize);

            Page<Person> people_page;
            if (keyword == null)
                people_page = personRepository.findAll(paging);
            else {
                people_page = personRepository.findByFirstNameContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }

            people = people_page.getContent();

            model.addAttribute("people", people);
            model.addAttribute("currentPage", people_page.getNumber() + 1);
            model.addAttribute("totalItems", people_page.getTotalElements());
            model.addAttribute("totalPages", people_page.getTotalPages());
            model.addAttribute("pageSize", pageSize);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "lists/people";
    }





    @PostMapping("/person/insert")
    public String insert_person
    (   Model model
    ,   @Valid @ModelAttribute Person person
    ,   BindingResult bindingResult
    ) {
        String message="";
        model.addAttribute("companies", companyRepo.findAll());
        model.addAttribute("groups", groupRepo.findAll());
        if (bindingResult.hasErrors())
            return "forms/person_form";

        personRepository.save(person);
        return "redirect:/people";
    }


    @GetMapping(value = "person/new")
    public String person_new
            (   Model model
            ){
        Person pers = new Person();
        pers.setCompany(new Company());
        pers.getCompany().setId(0L);
        model.addAttribute("companies", companyRepo.findAll());
        model.addAttribute("available_groups", groupRepo.findAll());
        model.addAttribute("person", pers);
        return "forms/person_form";
    }

    @GetMapping("/people/edit/{personId}")
    public String person_edit
    (   Model model
    ,   @PathVariable Long personId
    ){
        Person pers = personRepository.findById(personId).get();
        if(null==pers.getCompany()){
            pers.setCompany(new Company());
            pers.getCompany().setId(0L);
        }
        List<Group> l_available_groups= groupRepo.findAll();
        for(Group grp: pers.getGroups())
            if(l_available_groups.contains(grp))
                l_available_groups.remove(grp);
        model.addAttribute("person", pers);
        model.addAttribute("companies", companyRepo.findAll());
        model.addAttribute("available_groups", l_available_groups);
        model.addAttribute("member_of", pers.getGroups());
        return "forms/person_form";
    }


    @GetMapping("/people/delete/{personId}")
    public String person_delete
    (   Model model
    ,   @PathVariable Long personId
    ){
        personRepository.deleteById(personId);
        return "redirect:/people";
    }





    @GetMapping("/people/{personId}/published/{status}")
    public String company_edit
    (   Model model
    ,   @PathVariable Long personId
    ,   @PathVariable("status") boolean published
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ){
        Person pers = null;
        try {
            pers = personRepository.findById(personId).orElseThrow(() -> new Exception("`" + personId + "`: no such person ID"));
            pers.setActive(published);
            personRepository.save(pers);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return getAll(model, keyword, page, pageSize);
    }

    @PostMapping(path= "/person/{personId}/group/{action}", produces = MediaType.TEXT_HTML_VALUE)
    public String person_groups
    (   Model model
    ,   @PathVariable("personId")   Long personId
    ,   @PathVariable("action")   boolean action
    ){
        return "CACCA";
    }

}
