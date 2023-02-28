package com.davidauz.bulk_mailing.controller;

import com.davidauz.bulk_mailing.entity.Nation;
import com.davidauz.bulk_mailing.repository.NationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NationsController {

    @Autowired
    private NationRepository nationRepository;


    @PostMapping(path="/nation/add", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody // returning raw content and not a template name
    public String nation_add
    (   Model model
    ,   @RequestParam("new_nation") String new_nation
    ){
        Nation nat = new Nation();
        nat.setNation(new_nation);
        nationRepository.save(nat);
        return getNationsOptions();
    }

    private String getNationsOptions() {
        String strToRet="<option value=\"0\">--SELECT--</option>";
        for(Nation nat : nationRepository.findAll() )
            strToRet = strToRet+"<option value="+nat.getNationId()+">"+nat.getNation()+"</option>";
        return strToRet;
    }

}
