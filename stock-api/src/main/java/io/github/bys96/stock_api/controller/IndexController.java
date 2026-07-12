package io.github.bys96.stock_api.controller;

import io.github.bys96.stock_api.entity.StockIndex;
import io.github.bys96.stock_api.service.IndexService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/index")
public class IndexController {

    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * 1년 동기화
     */
    @PostMapping("/sync")
    public String sync() {

        indexService.syncMissingData();

        return "Sync Complete";
    }

    /**
     * 하루 조회
     * ex)
     * /api/index?market=KOSPI&tradeDate=20260709
     */
    @GetMapping
    public StockIndex getIndex(
            @RequestParam String market,
            @RequestParam String tradeDate) {

        return indexService.getIndex(
                market,
                tradeDate
        );
    }

    /**
     * 차트용
     * ex)
     * /api/index/history?market=KOSPI
     */
    @GetMapping("/history")
    public List<StockIndex> history(
            @RequestParam String market,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return indexService.getHistory(
                market,
                startDate,
                endDate
        );
    }

    /**
     * 전체
     */
    @GetMapping("/history/all")
    public List<StockIndex> allHistory() {

        return indexService.getAllHistory();
    }

}