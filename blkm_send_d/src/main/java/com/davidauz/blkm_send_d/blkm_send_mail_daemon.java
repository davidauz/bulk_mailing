package com.davidauz.blkm_send_d;

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

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@SpringBootApplication(scanBasePackages = "com.davidauz.blkm_common")
@EntityScan(basePackages={"com.davidauz.blkm_common"})
@ComponentScan(basePackages={"com.davidauz.bulk_mailing", "com.davidauz.blkm_common", "com.davidauz.blkm_send_d"})
@EnableJpaRepositories(basePackages = "com.davidauz.blkm_common")
@EnableScheduling
public class blkm_send_mail_daemon extends SpringBootServletInitializer  {
// SpringBootServletInitializer is for running in Tomcat

    public static void main(String[] args) {
        new SpringApplicationBuilder(blkm_send_mail_daemon.class)
//                .web(WebApplicationType.NONE)
//                .sources(blkm_send_mail_daemon.class)
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
    public Runnable get_sm_background_task() {
        return new blkm_send_task();
    }

    @Bean
    public ScheduledFuture<?> scheduleMyBackgroundTask(TaskScheduler taskScheduler) {
        Duration dur= Duration.ofSeconds(5);
        return taskScheduler.scheduleAtFixedRate(get_sm_background_task(), dur);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
// also for running in Tomcat
        return builder.sources(blkm_send_mail_daemon.class);
    }
}

