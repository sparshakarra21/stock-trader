package com.stocktrading.controller;

import com.stocktrading.model.Portfolio;
import com.stocktrading.model.User;
import com.stocktrading.repository.PortfolioRepository;
import com.stocktrading.repository.TransactionRepository;
import com.stocktrading.repository.UserRepository;
import com.stocktrading.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @GetMapping
    public Map<String, Object> getPortfolio(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Portfolio> holdings = portfolioRepository.findByUser(user);
        List<Map<String, Object>> holdingDtos = holdings.stream().map(p -> Map.<String, Object>of(
                "stockSymbol", p.getStock().getSymbol(),
                "stockName", p.getStock().getName(),
                "quantity", p.getQuantity(),
                "currentPrice", p.getStock().getPrice(),
                "totalValue", p.getStock().getPrice().multiply(BigDecimal.valueOf(p.getQuantity()))
        )).toList();
        return Map.of("balance", user.getBalance(), "holdings", holdingDtos);
    }

    @GetMapping("/transactions")
    public List<Map<String, Object>> getTransactions(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return transactionRepository.findByUserOrderByTimestampDesc(user).stream().map(tx -> Map.<String, Object>of(
                "type", tx.getType().name(),
                "stockSymbol", tx.getStock().getSymbol(),
                "quantity", tx.getQuantity(),
                "pricePerShare", tx.getPricePerShare(),
                "totalAmount", tx.getTotalAmount(),
                "timestamp", tx.getTimestamp().toString()
        )).toList();
    }
}
