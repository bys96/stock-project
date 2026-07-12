package io.github.bys96.stock_api.service;

import io.github.bys96.stock_api.entity.StockIndex;
import io.github.bys96.stock_api.repository.StockIndexRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class IndexService {

    private final StockIndexRepository repository;
    private final KrxApiService krxApiService;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyyMMdd");


    public IndexService(
            StockIndexRepository repository,
            KrxApiService krxApiService
    ) {
        this.repository = repository;
        this.krxApiService = krxApiService;
    }


    /**
     * 누락 데이터 동기화
     */
    public void syncMissingData() {

        LocalDate startDate = getStartDate();

        LocalDate today = LocalDate.now();


        while (!startDate.isAfter(today)) {

            String tradeDate = startDate.format(formatter);


            // KOSPI
            saveIfNotExists(
                    "KOSPI",
                    tradeDate
            );


            // KOSDAQ
            saveIfNotExists(
                    "KOSDAQ",
                    tradeDate
            );


            startDate = startDate.plusDays(1);
        }

    }


    /**
     * 시작 날짜 결정
     */
    private LocalDate getStartDate() {

        Optional<StockIndex> latest =
                repository.findTopByOrderByTradeDateDesc();


        // DB 데이터 없음
        if (latest.isEmpty()) {

            return LocalDate.now()
                    .minusYears(1);

        }


        // 마지막 날짜 다음날부터
        return LocalDate.parse(
                latest.get().getTradeDate(),
                formatter
        ).plusDays(1);

    }


    /**
     * 데이터 없으면 저장
     */
    private void saveIfNotExists(
            String market,
            String tradeDate
    ) {


        if (repository.existsByMarketAndTradeDate(
                market,
                tradeDate
        )) {

            return;

        }


        StockIndex data =
                krxApiService.getIndex(
                        market,
                        tradeDate
                );


        if (data != null) {

            repository.save(data);

        }

    }


    /**
     * 하루 조회
     */
    public StockIndex getIndex(
            String market,
            String tradeDate
    ) {

        return repository
                .findByMarketAndTradeDate(
                        market,
                        tradeDate
                )
                .orElse(null);

    }


    /**
     * 차트 데이터
     */
    public List<StockIndex> getHistory(
            String market,
            String startDate,
            String endDate
    ) {

        return repository
                .findByMarketAndTradeDateBetweenOrderByTradeDateAsc(
                        market,
                        startDate,
                        endDate
                );

    }


    public List<StockIndex> getAllHistory() {

        return repository
                .findAllByOrderByTradeDateAsc();

    }

}