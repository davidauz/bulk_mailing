package com.davidauz.blkm_interface.controller;

import com.davidauz.blkm_common.entity.blk_MailMessage;
import com.davidauz.blkm_common.entity.blk_MailQueue;
import com.davidauz.blkm_common.entity.reportsDTO;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import com.davidauz.blkm_interface.entity.*;
import com.davidauz.blkm_interface.repository.*;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class reportsController {

    @Autowired
    private MailMessageRepository msgRepo;

    @GetMapping("/reports")
    public String getAll
    (	Model model
    ,	@RequestParam(defaultValue = "1") int page
    ,	@RequestParam(defaultValue = "30") int pageSize
    ,	@RequestParam(required=false) String Project
    ,	@RequestParam(required=false) String Subject
    ,	@RequestParam(required=false) String Addresse
    ,	@RequestParam(required=false) String Status
    ) {
        try {
            String name ="UNKNOWN";
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth != null) {
                name = auth.getName();
            }else
                name ="AUTH is NULL";

            List<reportsDTO> msg_list = new ArrayList<>();
            Pageable paging = PageRequest.of(page - 1, pageSize);
            List<Object> msg_page;
            msg_page = msgRepo.findByParameters(Project, Subject, Addresse, Status);
			model.addAttribute("Project", Project);
            model.addAttribute("Subject", Subject);
            model.addAttribute("Addresse", Addresse);
            model.addAttribute("Status", Status);
//			msg_list = msg_page.getContent();
            model.addAttribute("reports_object", msg_list);
//			model.addAttribute("currentPage", msg_page.getNumber() + 1);
//			model.addAttribute("totalItems", msg_page.getTotalElements());
//			model.addAttribute("totalPages", msg_page.getTotalPages());
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("s_auth_message", name );
        } catch (Exception e) {
            model.addAttribute("error_message", e.getMessage());
        }

        return "lists/report";
    }


}


