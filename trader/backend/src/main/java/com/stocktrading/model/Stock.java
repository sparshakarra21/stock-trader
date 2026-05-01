package com.stocktrading.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer availableShares;

    @Column(nullable = false)
    private Integer totalShares;

    public Stock(String symbol, String name, BigDecimal price, Integer totalShares) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.totalShares = totalShares;
        this.availableShares = totalShares;
    }

    public BigDecimal getPurchasePercentage() {
        if (totalShares == 0) return BigDecimal.ZERO;
        int purchased = totalShares - availableShares;
        return new BigDecimal(purchased).multiply(new BigDecimal("100")).divide(new BigDecimal(totalShares), 2, java.math.RoundingMode.HALF_UP);
    }
}
