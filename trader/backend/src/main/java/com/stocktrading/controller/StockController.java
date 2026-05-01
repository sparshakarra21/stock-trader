package com.stocktrading.controller;

import com.stocktrading.dto.StockDTO;
import com.stocktrading.dto.TradeRequest;
import com.stocktrading.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public List<StockDTO> getAllStocks() {
        return stockService.getAllStocks();
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(@AuthenticationPrincipal UserDetails user,
                                       @RequestBody TradeRequest request) {
        try {
            stockService.buyStock(user.getUsername(), request.getStockId(), request.getQuantity());
            return ResponseEntity.ok(Map.of("message", "Purchase successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sellStock(@AuthenticationPrincipal UserDetails user,
                                        @RequestBody TradeRequest request) {
        try {
            stockService.sellStock(user.getUsername(), request.getStockId(), request.getQuantity());
            return ResponseEntity.ok(Map.of("message", "Sale successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
