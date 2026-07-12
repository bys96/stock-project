package io.github.bys96.stock_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KrxIndexDto {

    private String market;

    private String tradeDate;

    private Double openPrice;

    private Double highPrice;

    private Double lowPrice;

    private Double closePrice;

    private Double changePrice;

    private Double changeRate;

    private Long volume;

    private Long tradeValue;

    private Long marketCap;

}