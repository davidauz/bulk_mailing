package com.davidauz.blkm_send_d;

import com.davidauz.blkm_common.repo.ConfigurationRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@SpringBootApplication(scanBasePackages = "com.davidauz.blkm_common") //  this annotation is a composition of the
// @Configuration,
// @ComponentScan,
// and @EnableAutoConfiguration annotations.
// With this default setting, Spring Boot will auto scan for components in the current package
// (containing the @SpringBoot main class) and its sub packages.
@EntityScan(basePackages={"com.davidauz.blkm_common"})
@ComponentScan(basePackages={"com.davidauz.bulk_mailing", "com.davidauz.blkm_common", "com.davidauz.blkm_send_d"})
@EnableJpaRepositories(basePackages = "com.davidauz.blkm_common")
@EnableScheduling
public class blkm_send_mail_daemon extends SpringBootServletInitializer  {
// SpringBootServletInitializer is for running in Tomcat

    @Autowired
    private ConfigurationRepository cfgRepo;

    public static void main(String[] args) {
        new SpringApplicationBuilder(blkm_send_mail_daemon.class)
                .web(WebApplicationType.NONE)
                .sources(blkm_send_mail_daemon.class)
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
        return new blkm_send_task();
    }

    @Bean
    public ScheduledFuture<?> scheduleMyBackgroundTask(TaskScheduler taskScheduler) {
        Duration dur= Duration.ofSeconds(5);
        return taskScheduler.scheduleAtFixedRate(get_md_background_task(), dur);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
// also for running in Tomcat
        return builder.sources(blkm_send_mail_daemon.class);
    }
}

