package com.stocktrading.service;

import com.stocktrading.model.Stock;
import com.stocktrading.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class StockPriceSimulator {

    private final StockRepository stockRepository;
    private final StockService stockService;
    private final Random random = new Random();

    @Scheduled(fixedRate = 3000)
    public void simulatePriceChanges() {
        List<Stock> stocks = stockRepository.findAll();
        if (stocks.isEmpty()) return;

        for (Stock stock : stocks) {
            double changePercent = (random.nextDouble() - 0.5) * 0.04; // -2% to +2%
            BigDecimal change = stock.getPrice().multiply(BigDecimal.valueOf(changePercent));
            BigDecimal newPrice = stock.getPrice().add(change).setScale(2, RoundingMode.HALF_UP);
            if (newPrice.compareTo(BigDecimal.ONE) < 0) {
                newPrice = BigDecimal.ONE;
            }
            stock.setPrice(newPrice);
        }
        stockRepository.saveAll(stocks);
        stockService.broadcastStockUpdate();
    }
}
