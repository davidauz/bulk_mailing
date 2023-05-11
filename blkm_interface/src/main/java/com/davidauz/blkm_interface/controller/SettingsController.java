package com.davidauz.blkm_interface.controller;

import com.davidauz.blkm_common.entity.ConfigurationPair;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SettingsController {//TODO: move unrelated messages to a configurable folder

    @Autowired
    ConfigurationRepository cfgRepo;

    private void addUserName(Model model)
    {
        String name ="UNKNOWN";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
            name = auth.getName();
        }else
            name ="AUTH is NULL";
        model.addAttribute("s_auth_message", name );
    }

    @GetMapping("/settings")
    public String settings_main
    (Model model
    ) {
        addUserName(model);
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
    ,   @RequestParam(name="check_x_minutes", required=false) String check_x_minutes
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
            ,	{"check_x_minutes", check_x_minutes}
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


    @PostMapping(path = "settings/sendingRules", produces = MediaType.TEXT_HTML_VALUE)
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



    @PostMapping(path = "settings/mailServer", produces = MediaType.TEXT_HTML_VALUE)
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


    @PostMapping("/settings/ajx_dispatcher")
    public ResponseEntity<String> handleAjxRequest
    (   @RequestBody Map<String, Object> requestData
    ) {
        String str_verb= (String) requestData.get("verb");
        if (str_verb == null)
            return ResponseEntity.badRequest().body("VERB parameter is missing");
        switch(str_verb){
            case "heartbeats":
                return heartbeat_data();
        }
        return ResponseEntity.ok("success");
    }

    private ResponseEntity<String> heartbeat_data() {
        String jsonString = null
        ,   read_color=""
        ,   send_color=""
        ;
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Object> ret_data=new HashMap<>();
        Optional<ConfigurationPair> rmdr=cfgRepo.findByName("read_mail_daemon_running")
        ,   smdr=cfgRepo.findByName("send_mail_daemon_running")
        ,   hbr=cfgRepo.findByName("heartbeat_r")
        ,   hbs=cfgRepo.findByName("heartbeat_s")
        ;
        if(rmdr.isPresent()){
            if("0".equals((String)(rmdr.get().getValue())))
                read_color="#000000";
            else
                read_color="#FF0000";
        } else
            read_color="#a0a0a0";

        if(smdr.isPresent()){
            if("0".equals((String)(smdr.get().getValue())))
                send_color="#000000";
            else
                send_color="#FF0000";
        } else
            send_color="#a0a0a0";

        if(hbr.isPresent())
            ret_data.put("HBR",hbr.get());
        else
            ret_data.put("HBR","0");

        if(hbs.isPresent())
            ret_data.put("HBS",hbr.get());
        else
            ret_data.put("HBS","0");

        ret_data.put("SEND",send_color);
        ret_data.put("READ",read_color);

        try {
            jsonString = objectMapper.writeValueAsString(ret_data);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.OK);
    }

}

