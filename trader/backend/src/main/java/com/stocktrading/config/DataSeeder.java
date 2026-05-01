package com.stocktrading.config;

import com.stocktrading.model.Stock;
import com.stocktrading.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final StockRepository stockRepository;

    @Override
    public void run(String... args) {
        if (stockRepository.count() == 0) {
            stockRepository.saveAll(List.of(
                new Stock("AAPL", "Apple Inc.", new BigDecimal("178.50"), 10000),
                new Stock("GOOGL", "Alphabet Inc.", new BigDecimal("141.25"), 8000),
                new Stock("MSFT", "Microsoft Corp.", new BigDecimal("378.90"), 12000),
                new Stock("AMZN", "Amazon.com Inc.", new BigDecimal("185.60"), 9000),
                new Stock("TSLA", "Tesla Inc.", new BigDecimal("245.30"), 7000),
                new Stock("META", "Meta Platforms", new BigDecimal("505.75"), 6000),
                new Stock("NVDA", "NVIDIA Corp.", new BigDecimal("875.40"), 5000),
                new Stock("NFLX", "Netflix Inc.", new BigDecimal("628.90"), 4000)
            ));
        }
    }
}
