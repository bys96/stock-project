package io.github.bys96.stock_api.repository;

import io.github.bys96.stock_api.entity.StockIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockIndexRepository extends JpaRepository<StockIndex, Long> {

    List<StockIndex> findByTradeDate(String tradeDate);

    Optional<StockIndex> findTopByOrderByTradeDateDesc();

    Optional<StockIndex> findByMarketAndTradeDate(
            String market,
            String tradeDate
    );

    boolean existsByMarketAndTradeDate(
            String market,
            String tradeDate
    );

    List<StockIndex> findByMarketOrderByTradeDateAsc(String market);

    List<StockIndex> findByMarketAndTradeDateBetweenOrderByTradeDateAsc(
            String market,
            String startDate,
            String endDate
    );

    List<StockIndex> findAllByOrderByTradeDateAsc();

    @Query("""
        select concat(s.tradeDate,'_',s.market)
        from StockIndex s
    """)
    List<String> findAllKeys();

}