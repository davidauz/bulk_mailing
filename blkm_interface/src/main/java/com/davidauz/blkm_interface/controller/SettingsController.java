package com.davidauz.blkm_interface.controller;

import com.davidauz.blkm_common.entity.ConfigurationPair;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SettingsController {

    @Autowired
    ConfigurationRepository cfgRepo;

    @GetMapping("/settings")
    public String groups_search
    (Model model
    ) {
        Map<String, String> settings_map = new HashMap<>();
        List<ConfigurationPair> all_settings = cfgRepo.findAll();
        for (ConfigurationPair keyvalue : all_settings)
            settings_map.put(keyvalue.getName(), keyvalue.getValue());

        model.addAttribute("settings_map", settings_map);
        return "forms/settings_form";
    }




    @PostMapping(path = "/settings/readingRules", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody // returning raw content and not a template name
    public String save_reading_rules_settings
    (   Model model
    ,   @RequestParam("imap_server") String imap_server
    ,   @RequestParam("imap_server_port") String imap_server_port
    ,   @RequestParam(name="imap_ssl", required=false) String imap_ssl
    ,   @RequestParam(name="imap_user_same", required=false) String imap_user_same
    ,   @RequestParam(name="imap_uname", required=false) String imap_uname
    ,   @RequestParam(name="imap_password", required=false) String imap_password
    ,   @RequestParam(name="imap_password2", required=false) String imap_password2
    ) {
        try {
            try { Integer.parseInt(imap_server_port); }
            catch (NumberFormatException e){
                throw new Exception("`"+imap_server_port+"` is not a number");
            }
            if(null!=imap_user_same && imap_user_same.equals("on") ){
                imap_uname=cfgRepo.findByName("serveruname").orElseThrow(() ->new Exception("IMAP user same as SMTP user but SMTP user name not found")).getValue();
                imap_password=cfgRepo.findByName("serverpassword").orElseThrow(() ->new Exception("IMAP password same as SMTP password  but SMTP password  name not found")).getValue();
            }

            String[][] configs = new String[][]
            {	{"imap_server", imap_server}
            ,	{"imap_server_port", imap_server_port}
            ,	{"imap_user_same", imap_user_same}
            ,	{"imap_ssl", imap_ssl}
            ,	{"imap_uname", imap_uname}
            ,	{"imap_password", imap_password}
            ,	{"imap_password2", imap_password2}
            };

            for (String[] strp : configs) {
                Optional<ConfigurationPair> cfg = cfgRepo.findByName(strp[0]);
                ConfigurationPair cp = null;
                if (cfg.isPresent()) {
                    cp = cfg.get();
                    cp.setValue(strp[1]);
                } else {
                    cp = new ConfigurationPair();
                    cp.setName(strp[0]);
                    cp.setValue(strp[1]);
                }
                cfgRepo.save(cp);
            }
            return " Changes were saved";
        } catch (Exception e) {
            return "Changes were NOT saved because: " + e.getMessage();
        }
    }


    @PostMapping(path = "/settings/sendingRules", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody // returning raw content and not a template name
    public String save_sending_rules_settings
    (Model model
    ,   @RequestParam("sendingRandomDelay") String sendingRandomDelay
    ,   @RequestParam("batchSize") String batchSize
    ,   @RequestParam("minPauseBatch") String minPauseBatch
    ,   @RequestParam("maxMessagesInADay") String maxMessagesInADay
    ,   @RequestParam("message_send_randomize") String message_send_randomize
    ) {
        try {
            String[][] configs = new String[][]
            {{"sendingRandomDelay", sendingRandomDelay}
            , {"batchSize", batchSize}
            , {"minPauseBatch", minPauseBatch}
            , {"maxMessagesInADay", maxMessagesInADay}
            , {"message_send_randomize", message_send_randomize}
            };
            for (String[] strp : configs) {
                Optional<ConfigurationPair> cfg = cfgRepo.findByName(strp[0]);
                ConfigurationPair cp = null;
                if (cfg.isPresent()) {
                    cp = cfg.get();
                    cp.setValue(strp[1]);
                } else {
                    cp = new ConfigurationPair();
                    cp.setName(strp[0]);
                    cp.setValue(strp[1]);
                }
                cfgRepo.save(cp);
            }
            return " Changes were saved";
        } catch (Exception e) {
            return "Changes were NOT saved because: " + e.getMessage();
        }
    }



    @PostMapping(path = "/settings/mailServer", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody // returning raw content and not a template name
    public String save_mail_server_settings
    (Model model
    ,   @RequestParam("SMTPServer") String SMTPServer
    ,   @RequestParam("SMTPPort") String SMTPPort
    ,   @RequestParam("serveruname") String serveruname
    ,   @RequestParam("serverpassword") String serverpassword
    ,   @RequestParam("serverpassword2") String serverpassword2
    ,   @RequestParam("authtype") String authtype
    ) {
        try {
            if (!serverpassword.equals(serverpassword2))
                throw new Exception("the given passwords do not match");
            try { Integer.parseInt(SMTPPort); }
            catch (NumberFormatException e){
                throw new Exception("`"+SMTPPort+"` is not a number");
            }

            String[][] configs = new String[][]
            {	{"SMTPServer", SMTPServer}
            ,	{"SMTPPort", SMTPPort}
            ,	{"serveruname", serveruname}
            ,	{"serverpassword", serverpassword}
            ,	{"serverpassword2", serverpassword2}
            ,	{"authtype", authtype}
            };
            for (String[] strp : configs) {
                Optional<ConfigurationPair> cfg = cfgRepo.findByName(strp[0]);
                ConfigurationPair cp = null;
                if (cfg.isPresent()) {
                    cp = cfg.get();
                    cp.setValue(strp[1]);
                } else {
                    cp = new ConfigurationPair();
                    cp.setName(strp[0]);
                    cp.setValue(strp[1]);
                }
                cfgRepo.save(cp);
            }
            return " Changes were saved";
        } catch (Exception e) {
            return "Changes were NOT saved because: " + e.getMessage();
        }
    }



    @PostMapping(path = "/settings/toggle/{toggle_setting}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String checkbox
    (   @RequestParam Map<String, String> parameters
    ) {
        if( ! parameters.containsKey("checkbox") )
            return "ERROR: parameter not found";

        String control_ID=parameters.get("checkbox")
        ,   control_value=parameters.get(control_ID)
        ;
        ConfigurationPair cfg;
        Optional<ConfigurationPair> o_cfg = cfgRepo.findByName(control_ID);
        if(o_cfg.isPresent())
            cfg=o_cfg.get();
        else{
            cfg=new ConfigurationPair();
            cfg.setName(control_ID);
        }
        if (null!=control_value && control_value.equals("on")) {
                cfg.setValue("1");
                cfgRepo.save(cfg);
                return "active";
        }
        cfg.setValue("0");
        cfgRepo.save(cfg);
        return "NOT active";
    }




}

