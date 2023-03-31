package com.davidauz.blkm_send_d.config;

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
