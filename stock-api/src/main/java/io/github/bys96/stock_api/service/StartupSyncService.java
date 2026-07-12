package io.github.bys96.stock_api.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class StartupSyncService {

    private final IndexService indexService;

    public StartupSyncService(IndexService indexService) {
        this.indexService = indexService;
    }

    @Async
    public void sync() {

        System.out.println("=== 시작 데이터 동기화 시작 ===");

        indexService.syncMissingData();

        System.out.println("=== 시작 데이터 동기화 완료 ===");
    }
}