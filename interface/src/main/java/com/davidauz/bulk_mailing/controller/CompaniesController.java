package com.davidauz.bulk_mailing.controller;

import com.davidauz.bulk_mailing.entity.Company;
import com.davidauz.bulk_mailing.repository.CompanyRepository;
import com.davidauz.bulk_mailing.repository.NationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/companies/search")
    public String companies_search
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ){
        return getAll(model, keyword,page,pageSize);
    }


    @GetMapping("/companies")
    public String getAll
    (   Model model
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ) {
        try {
            List<Company> companies = new ArrayList<Company>();
            Pageable paging = PageRequest.of(page - 1, pageSize);

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





    @PostMapping("/company_insert")
    public String insert_company
    (   Model model
    ,   @Valid @ModelAttribute Company company
    ,   BindingResult bindingResult
    ) {
        String message="";
        if (bindingResult.hasErrors()) {
            model.addAttribute("nations", nationRepo.findAll());
            return "forms/company_form";
        }
        companyRepository.save(company);
        return "redirect:/companies";
    }

    @GetMapping("/companies/edit/{companyId}")
    public String company_edit
    (   Model model
    ,   @PathVariable Long companyId
    ){
        Company comp = companyRepository.findById(companyId).get();
        model.addAttribute("nations", nationRepo.findAll());
        model.addAttribute("company", comp);
        return "forms/company_form";
    }


    @GetMapping("/companies/delete/{companyId}")
    public String company_delete
    (   Model model
            ,   @PathVariable Long companyId
    ){
        companyRepository.deleteById(companyId);
        return "companies";
    }



    @GetMapping("/companies/{companyId}/published/{status}")
    public String company_enable_disable
    (   Model model
    ,   @PathVariable Long companyId
    ,   @PathVariable("status") boolean published
    ,   @RequestParam(required = false) String keyword
    ,   @RequestParam(defaultValue = "1") int page
    ,   @RequestParam(defaultValue = "30") int pageSize
    ){
        try {
            Company com = companyRepository.findById(companyId).orElseThrow(() -> new Exception("`" + companyId + "`: no such company ID"));
            com.setActive(published);
            companyRepository.save(com);
        } catch(Exception e){
            model.addAttribute("message", e.getMessage());
        }
        return getAll(model, keyword, page, pageSize);
    }



    @GetMapping(value = "/company_new")
    public String company_new
    (   Model model
    ){
        model.addAttribute("nations", nationRepo.findAll());
        model.addAttribute("company", new Company());
        return "forms/company_form";
    }

}
