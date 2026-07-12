package io.github.bys96.stock_api.scheduler;

import io.github.bys96.stock_api.service.IndexService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IndexScheduler {

    private final IndexService indexService;

    public IndexScheduler(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * 매일 오후 6시 실행
     * (KRX 장 마감 후 데이터 반영)
     */
    @Scheduled(cron = "0 0 18 * * *")
    public void syncDaily() {

        System.out.println("=== 스케줄러 동기화 시작 ===");

        indexService.syncMissingData();

        System.out.println("=== 스케줄러 동기화 완료 ===");
    }

}