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

	@RequestMapping("/reports")
	public String getAll
	(	Model model
	,	@RequestParam(defaultValue = "1") int page
	,	@RequestParam(defaultValue = "30") int pageSize
	,	@RequestParam(required=false) String Project
	,	@RequestParam(required=false) String Subject
	,	@RequestParam(required=false) String Addressee
	,	@RequestParam(required=false) String Status
	) {
		List<reportsDTO> msg_list = new ArrayList<>();
		String name ="UNKNOWN";
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth != null) {
				name = auth.getName();
			}else
				name ="AUTH is NULL";

			List<Object> msg_page;

			msg_page = msgRepo.findByParameters
			( (Project==null || 0== Project.length() ?null:Project)
			, (Subject == null || 0== Subject.length() ?null:Subject)
			, (Addressee ==null || 0== Addressee.length() ?null:Addressee)
			, (Status ==null || 0== Status.length() ?null:Status)
			);
			for(Object objx: msg_page) {
				Object[] obja = (Object[]) objx;

				msg_list.add(new reportsDTO
				((Long)obja[0]		// dto ID (useless)
				,(Long)obja[1]		// projectId
				,(String)obja[2]    // description
				,(String)obja[3]    // Result
				,(String)obja[4]   // recipient
				,(Long)obja[5]      // idRecipient
				,(String)obja[6]    // sentStatus
				,(String)obja[7]    // subject
				));
			}

		} catch (Exception e) {
			model.addAttribute("error_message", e.getMessage());
		}

		model.addAttribute("Project", Project);
		model.addAttribute("Subject", Subject);
		model.addAttribute("Addressee", Addressee);
		model.addAttribute("Status", Status);
		model.addAttribute("reports_object", msg_list);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalItems", 5);
		model.addAttribute("totalPages", 5);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("s_auth_message", name );

		return "lists/report";
	}

	@GetMapping("/reports/delete")
	public String deleteMessage
	(	Model model
	,	@RequestParam(value="pg",defaultValue = "1") int page
	,	@RequestParam(value="ps",defaultValue = "30") int pageSize
	,	@RequestParam(value="pr",required=false) String Project
	,	@RequestParam(value="su",required=false) String Subject
	,	@RequestParam(value="ad",required=false) String Addressee
	,	@RequestParam(value="st",required=false) String Status
	,	@RequestParam(value="de",required=false) long delId
	){
		return getAll(model, page, pageSize, Project, Subject, Addressee, Status );
	}


}





