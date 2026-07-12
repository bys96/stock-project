package io.github.bys96.stock_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "stock_index",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"market", "trade_date"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 시장(KOSPI, KOSDAQ)
    @Column(nullable = false, length = 20)
    private String market;

    // 거래일
    @Column(name = "trade_date", nullable = false, length = 8)
    private String tradeDate;

    // 시가
    @Column(name = "open_price")
    private Double openPrice;

    // 고가
    @Column(name = "high_price")
    private Double highPrice;

    // 저가
    @Column(name = "low_price")
    private Double lowPrice;

    // 종가
    @Column(name = "close_price")
    private Double closePrice;

    // 전일대비
    @Column(name = "change_price")
    private Double changePrice;

    // 등락률
    @Column(name = "change_rate")
    private Double changeRate;

    // 거래량
    private Long volume;

    // 거래대금
    @Column(name = "trade_value")
    private Long tradeValue;

    // 시가총액
    @Column(name = "market_cap")
    private Long marketCap;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}