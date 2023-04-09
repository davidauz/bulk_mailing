package com.davidauz.blkm_interface.controller;

import com.davidauz.blkm_common.repo.MailMessageRepository;
import com.davidauz.blkm_interface.repository.CompanyRepository;
import com.davidauz.blkm_interface.repository.PersonRepository;
import com.davidauz.blkm_interface.service.UserService;
import com.davidauz.blkm_interface.repository.CompanyRepository;
import com.davidauz.blkm_interface.repository.PersonRepository;
import com.davidauz.blkm_interface.service.UserService;
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


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MailMessageRepository mmrepo;


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
        model.addAttribute("num_messages", mmrepo.count());
        model.addAttribute("num_companies", companyRepository.count());
        model.addAttribute("num_people", personRepository.count());

        return "dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model)
    {
        return home(model);
    }



}
