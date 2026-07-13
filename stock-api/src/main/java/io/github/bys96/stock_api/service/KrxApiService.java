package io.github.bys96.stock_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bys96.stock_api.entity.StockIndex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
public class KrxApiService {

    private final WebClient webClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${krx.api.key}")
    private String authKey;


    public KrxApiService(WebClient webClient) {
        this.webClient = webClient;
    }


    public StockIndex getIndex(
            String market,
            String tradeDate
    ) {

        String path;

        String targetName;


        if (market.equals("KOSPI")) {

            path = "/svc/apis/idx/kospi_dd_trd";
            targetName = "코스피";

        } else {

            path = "/svc/apis/idx/kosdaq_dd_trd";
            targetName = "코스닥";

        }


        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("basDd", tradeDate)
                        .build())
                .header(
                        "AUTH_KEY",
                        authKey
                )
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(20))
                .block();


        return parse(
                response,
                market,
                tradeDate,
                targetName
        );
    }



    private StockIndex parse(
            String json,
            String market,
            String tradeDate,
            String targetName
    ) {

        try {

            JsonNode root =
                    objectMapper.readTree(json);


            JsonNode arr =
                    root.path("OutBlock_1");


            for (JsonNode item : arr) {


                if (targetName.equals(
                        item.path("IDX_NM").asText()
                )) {


                    return StockIndex.builder()
                            .market(market)
                            .tradeDate(tradeDate)
                            .openPrice(item.path("OPNPRC_IDX").asDouble())
                            .highPrice(item.path("HGPRC_IDX").asDouble())
                            .lowPrice(item.path("LWPRC_IDX").asDouble())
                            .closePrice(item.path("CLSPRC_IDX").asDouble())
                            .changePrice(item.path("CMPPREVDD_IDX").asDouble())
                            .changeRate(item.path("FLUC_RT").asDouble())
                            .volume(item.path("ACC_TRDVOL").asLong())
                            .tradeValue(item.path("ACC_TRDVAL").asLong())
                            .marketCap(item.path("MKTCAP").asLong())
                            .build();

                }

            }


            return null;


        } catch (Exception e) {

            throw new RuntimeException(
                    "KRX JSON parsing error",
                    e
            );

        }

    }

}