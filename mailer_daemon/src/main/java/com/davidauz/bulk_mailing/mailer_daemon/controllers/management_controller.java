package com.davidauz.bulk_mailing.mailer_daemon.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@RestController
public class management_controller {

    ScheduledFuture<?> task;

    @Autowired
    private TaskScheduler taskScheduler;


    @GetMapping("/md/start")
    public String start() {
        Duration dur= Duration.ofSeconds(5);
        if (task == null || task.isCancelled()) {
            task = taskScheduler.scheduleAtFixedRate(this::real_task, dur);
        }
        return "<a href='http://localhost:8081/md/stop'>STOP</a>";
    }

    private void real_task(){

    }

    @GetMapping("/md/stop")
    public String stop() {
        if (task != null && !task.isCancelled()) {
            task.cancel(false);
        }
        return "<a href='http://localhost:8081/md/start'>START</a>";
    }
}
