package io.github.bys96.stock_api.runner;

import io.github.bys96.stock_api.service.StartupSyncService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final StartupSyncService startupSyncService;

    public StartupRunner(StartupSyncService startupSyncService) {
        this.startupSyncService = startupSyncService;
    }

    @Override
    public void run(String... args) {

        startupSyncService.sync();

    }

}