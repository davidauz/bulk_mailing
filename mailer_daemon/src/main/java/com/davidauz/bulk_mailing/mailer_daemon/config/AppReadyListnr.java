package com.davidauz.bulk_mailing.mailer_daemon.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AppReadyListnr {

    @EventListener(ApplicationReadyEvent.class)
    public void executeAfterStartup() {
// some stuff to do before startup
    }
}
