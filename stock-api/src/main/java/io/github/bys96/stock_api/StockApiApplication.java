package io.github.bys96.stock_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class StockApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockApiApplication.class, args);
	}

}
