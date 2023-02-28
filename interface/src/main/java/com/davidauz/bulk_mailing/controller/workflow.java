package com.davidauz.bulk_mailing.controller;

import com.davidauz.bulk_mailing.repository.CompanyRepository;
import com.davidauz.bulk_mailing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class workflow {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/")
    public String home
    (
            Model model
    ){
        String name ="UNKNOWN";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            name = auth.getName();
        }else
            name ="AUTH is NULL";
        model.addAttribute("s_auth_message", name );

        return "dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model)
    {
        return home(model);
    }



}
