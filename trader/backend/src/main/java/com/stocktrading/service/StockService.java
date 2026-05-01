package com.stocktrading.service;

import com.stocktrading.dto.StockDTO;
import com.stocktrading.model.*;
import com.stocktrading.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public List<StockDTO> getAllStocks() {
        return stockRepository.findAll().stream().map(StockDTO::from).toList();
    }

    @Transactional
    public void buyStock(String username, Long stockId, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        if (stock.getAvailableShares() < quantity) {
            throw new RuntimeException("Not enough shares available");
        }

        BigDecimal totalCost = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
        if (user.getBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(user.getBalance().subtract(totalCost));
        stock.setAvailableShares(stock.getAvailableShares() - quantity);

        Portfolio portfolio = portfolioRepository.findByUserAndStock(user, stock)
                .orElseGet(() -> {
                    Portfolio p = new Portfolio();
                    p.setUser(user);
                    p.setStock(stock);
                    p.setQuantity(0);
                    return p;
                });
        portfolio.setQuantity(portfolio.getQuantity() + quantity);

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setStock(stock);
        tx.setType(Transaction.TransactionType.BUY);
        tx.setQuantity(quantity);
        tx.setPricePerShare(stock.getPrice());
        tx.setTotalAmount(totalCost);

        userRepository.save(user);
        stockRepository.save(stock);
        portfolioRepository.save(portfolio);
        transactionRepository.save(tx);

        broadcastStockUpdate();
    }

    @Transactional
    public void sellStock(String username, Long stockId, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        Portfolio portfolio = portfolioRepository.findByUserAndStock(user, stock)
                .orElseThrow(() -> new RuntimeException("You don't own this stock"));

        if (portfolio.getQuantity() < quantity) {
            throw new RuntimeException("Not enough shares to sell");
        }

        BigDecimal totalValue = stock.getPrice().multiply(BigDecimal.valueOf(quantity));
        user.setBalance(user.getBalance().add(totalValue));
        stock.setAvailableShares(stock.getAvailableShares() + quantity);
        portfolio.setQuantity(portfolio.getQuantity() - quantity);

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setStock(stock);
        tx.setType(Transaction.TransactionType.SELL);
        tx.setQuantity(quantity);
        tx.setPricePerShare(stock.getPrice());
        tx.setTotalAmount(totalValue);

        userRepository.save(user);
        stockRepository.save(stock);
        portfolioRepository.save(portfolio);
        transactionRepository.save(tx);

        if (portfolio.getQuantity() == 0) {
            portfolioRepository.delete(portfolio);
        }

        broadcastStockUpdate();
    }

    public void broadcastStockUpdate() {
        List<StockDTO> stocks = getAllStocks();
        messagingTemplate.convertAndSend("/topic/stocks", stocks);
    }
}
