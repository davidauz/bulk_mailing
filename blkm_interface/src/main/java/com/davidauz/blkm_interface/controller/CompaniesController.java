package com.davidauz.blkm_interface.controller;

import com.davidauz.blkm_common.entity.Company;
import com.davidauz.blkm_common.repo.NationRepository;
import com.davidauz.blkm_common.repo.CompanyRepository;
import com.davidauz.blkm_common.service.AppLog;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CompaniesController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private NationRepository nationRepo;

    @Autowired private AppLog applog;

    private String addUserName(Model model)
    {
        String name ="UNKNOWN";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null)
            name = auth.getName();
        model.addAttribute("s_auth_message", name );
        return name;
    }

    @PostMapping("companies/search")
    public String companies_search
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int currentPage
    ,   @RequestParam(defaultValue = "30") int pageSize
    ){
        return getAll(model, keyword,currentPage,pageSize);
    }


    @PostMapping("companies/page/{pageNum}")
    public String companies_page
    (   Model model
            ,   @RequestParam(required = false) String keyword
            ,   @RequestParam(defaultValue = "1") int currentPage
            ,   @RequestParam(defaultValue = "30") int pageSize
            ,   @RequestParam(defaultValue = "0") int totalPages
            ,   @PathVariable String pageNum
    ){
        return companies_search(model, keyword,Integer.valueOf(pageNum),pageSize);
    }

    @PostMapping("companies/navigate/{direction}")
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
        return companies_search(model, keyword,currentPage,pageSize);
    }


    @GetMapping("/companies")
    public String getAll
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int currentPage
    ,   @RequestParam(defaultValue = "30") int pageSize
    ) {
        try {
            addUserName(model);

            List<Company> companies = new ArrayList<Company>();
            Pageable paging = PageRequest.of(currentPage - 1, pageSize);

            Page<Company> companies_page;
            if (keyword == null)
                companies_page = companyRepository.findAll(paging);
            else {
                companies_page = companyRepository.findByNameContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }

            companies = companies_page.getContent();

            model.addAttribute("companies", companies);
            model.addAttribute("currentPage", companies_page.getNumber() + 1);
            model.addAttribute("totalItems", companies_page.getTotalElements());
            model.addAttribute("totalPages", companies_page.getTotalPages());
            model.addAttribute("pageSize", pageSize);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "lists/companies";
    }





    @PostMapping("companies/insert")
    public String insert_company
    (   Model model
    ,   @Valid @ModelAttribute Company company
    ,   BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("nations", nationRepo.findAll());
            return "forms/company_form";
        }
        applog.log(addUserName(model)+" `"+company.getName()+"`");
        companyRepository.save(company);
        return "redirect:/companies";
    }

    @GetMapping("companies/edit/{companyId}")
    public String company_edit
    (   Model model
    ,   @PathVariable Long companyId
    ){
        Company comp = companyRepository.findById(companyId).get();
        applog.log(addUserName(model)+" `"+comp.getName()+"`");
        model.addAttribute("nations", nationRepo.findAll());
        model.addAttribute("company", comp);
        return "forms/company_form";
    }


    @GetMapping("companies/delete/{companyId}")
    public String company_delete
    (   Model model
    ,   @PathVariable Long companyId
    ){
        companyRepository.deleteById(companyId);
        applog.log(addUserName(model)+" `"+companyId+"`");
        return getAll(model, "", 1, 30);
    }



    @GetMapping("companies/published/{companyId}/{status}")
    public String company_enable_disable
    (   Model model
    ,   @PathVariable Long companyId
    ,   @PathVariable("status") boolean published
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int currentPage
    ,   @RequestParam(defaultValue = "30") int pageSize
    ){
        try {
            Company com = companyRepository.findById(companyId).orElseThrow(() -> new Exception("`" + companyId + "`: no such company ID"));
            com.setActive(published);
            companyRepository.save(com);
            applog.log(addUserName(model)+" `"+com.getName()+"`="+published);
        } catch(Exception e){
            model.addAttribute("message", e.getMessage());
        }
        return getAll(model, keyword, currentPage, pageSize);
    }



    @GetMapping(value = "company_new")
    public String company_new
    (   Model model
    ){
        applog.log(addUserName(model));
        model.addAttribute("nations", nationRepo.findAll());
        model.addAttribute("company", new Company());
        return "forms/company_form";
    }

}
