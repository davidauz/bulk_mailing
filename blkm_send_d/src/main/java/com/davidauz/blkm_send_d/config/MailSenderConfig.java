package com.davidauz.blkm_send_d.config;

import com.davidauz.blkm_common.entity.ConfigurationPair;
import com.davidauz.blkm_common.repo.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Optional;
import java.util.Properties;

@Configuration
public class MailSenderConfig {

    @Autowired
    private ConfigurationRepository cfgRepo;

    private static final String[][] MANDATORY_PROPERTIES =
    {	{"SMTPPort", "25"}
    ,	{"SMTPServer","example.smtp.com"}
    ,	{"authtype","STARTTLS"}
    ,	{"batchSize","5"}
    ,	{"maxMessagesInADay","19"}
    ,	{"minPauseBatch","5000"}
    ,	{"sendingRandomDelay","2000"}
    ,	{"serverpassword", "superSecret"}
    ,	{"serveruname", "myUserName"}
    ,	{"serveruname","user@smtp.server.com"}
    };

    private static final String[][] DEFAULT_PROPERTIES =
    {	{"heartbeat_s","0"}
    ,	{"send_mail_daemon_running","1"}
    };


    public void ensureValuesPresent() {
        ConfigurationPair cp;
        Optional<ConfigurationPair> cpo;
        for(String[] prop:MANDATORY_PROPERTIES) {
            cpo = cfgRepo.findByName(prop[0]);
            if(!cpo.isPresent()){
                cp=new ConfigurationPair();
                cp.setName(prop[0]);
                cp.setValue(prop[1]);
                cfgRepo.save(cp);
            }
        }

        for(String[] prop:DEFAULT_PROPERTIES) {
            cpo = cfgRepo.findByName(prop[0]);
            if(!cpo.isPresent()){
                cp=new ConfigurationPair();
                cp.setName(prop[0]);
                cp.setValue(prop[1]);
                cfgRepo.save(cp);
            } else{
                cp=cpo.get();
                cp.setValue(prop[1]);
                cfgRepo.save(cp);
            }
        }
    }


    @Bean("JavaMailSender") // this is because the parameters are read not from application.resources but
// from database, so here is the place where to configure the JavaMailSenderImpl (Autowired in sendEmailService)
    public JavaMailSenderImpl javaMailSenderImpl()  {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        ensureValuesPresent();
        try{
            mailSender.setUsername(cfgRepo.findByName("serveruname").get().getValue());
            mailSender.setHost(cfgRepo.findByName("SMTPServer").get().getValue());
            mailSender.setPort(Integer.parseInt(cfgRepo.findByName("SMTPPort").get().getValue()));
            mailSender.setUsername(cfgRepo.findByName("serveruname").get().getValue());
            mailSender.setPassword(cfgRepo.findByName("serverpassword").get().getValue());

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp"); // TODO: set this according to cfg
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return mailSender;
    }
}
