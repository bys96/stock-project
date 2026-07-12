package io.github.bys96.stock_api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bys96.stock_api.dto.KrxIndexDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KrxApiClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${krx.api.key}")
    private String authKey;

    public KrxApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public KrxIndexDto fetchKospi(String tradeDate) {
        return fetch("kospi_dd_trd", tradeDate, "코스피");
    }

    public KrxIndexDto fetchKosdaq(String tradeDate) {
        return fetch("kosdaq_dd_trd", tradeDate, "코스닥");
    }

    private KrxIndexDto fetch(String path, String tradeDate, String targetName) {

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/svc/apis/idx/" + path)
                        .queryParam("basDd", tradeDate)
                        .build())
                .header("AUTH_KEY", authKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return parse(response, targetName);
    }

    private KrxIndexDto parse(String json, String targetName) {

        try {

            JsonNode root = objectMapper.readTree(json);
            JsonNode arr = root.path("OutBlock_1");

            if (!arr.isArray() || arr.isEmpty()) {
                return null;
            }

            for (JsonNode item : arr) {

                if (!targetName.equals(item.path("IDX_NM").asText())) {
                    continue;
                }

                return KrxIndexDto.builder()
                        .market(item.path("IDX_CLSS").asText())
                        .tradeDate(item.path("BAS_DD").asText())
                        .openPrice(toDouble(item.path("OPNPRC_IDX").asText()))
                        .highPrice(toDouble(item.path("HGPRC_IDX").asText()))
                        .lowPrice(toDouble(item.path("LWPRC_IDX").asText()))
                        .closePrice(toDouble(item.path("CLSPRC_IDX").asText()))
                        .changePrice(toDouble(item.path("CMPPREVDD_IDX").asText()))
                        .changeRate(toDouble(item.path("FLUC_RT").asText()))
                        .volume(toLong(item.path("ACC_TRDVOL").asText()))
                        .tradeValue(toLong(item.path("ACC_TRDVAL").asText()))
                        .marketCap(toLong(item.path("MKTCAP").asText()))
                        .build();
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Double toDouble(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return Double.parseDouble(value.replace(",", ""));
    }

    private Long toLong(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return Long.parseLong(value.replace(",", ""));
    }
}