package com.davidauz.bulk_mailing.mailer_daemon.config;

import com.davidauz.bulk_mailing.common_classes.entity.ConfigurationPair;
import com.davidauz.bulk_mailing.common_classes.repo.ConfigurationRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppReadyListnr {

    @EventListener(ApplicationReadyEvent.class)
    public void executeAfterStartup() {
// some stuff to do before startup
    }
}
