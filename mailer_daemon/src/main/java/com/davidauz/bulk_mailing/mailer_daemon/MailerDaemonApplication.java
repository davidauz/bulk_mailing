package com.davidauz.bulk_mailing.mailer_daemon;

import com.davidauz.bulk_mailing.common_classes.repo.ConfigurationRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;

@SpringBootApplication(scanBasePackages = "com.davidauz.bulk_mailing.common_classes.*") //  this annotation is a composition of the
// @Configuration,
// @ComponentScan,
// and @EnableAutoConfiguration annotations.
// With this default setting, Spring Boot will auto scan for components in the current package
// (containing the @SpringBoot main class) and its sub packages.
@ComponentScan(basePackages={"com.davidauz.bulk_mailing.*", "com.davidauz.bulk_mailing.common_classes.*", "com.davidauz.bulk_mailing.mailer_daemon.config.*"})
@DependsOn("mailMessageRepository")
@EnableJpaRepositories(basePackages = "com.davidauz.bulk_mailing.common_classes")
@EnableScheduling
public class MailerDaemonApplication {

    @Autowired
    private ConfigurationRepository cfgRepo;

    public static void main(String[] args) {
        new SpringApplicationBuilder(MailerDaemonApplication.class)
                .web(WebApplicationType.SERVLET)
                .sources(MailerDaemonApplication.class)
                .run(args);
    }

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("mailer-daemon-scheduler-");
        scheduler.initialize();
        return scheduler;
    }

    @Bean
    public Runnable get_md_background_task() {
        return new md_background_task();
    }

    @Bean
    public ScheduledFuture<?> scheduleMyBackgroundTask(TaskScheduler taskScheduler) {
        Duration dur= Duration.ofSeconds(5);
        return taskScheduler.scheduleAtFixedRate(get_md_background_task(), dur);
    }
}

