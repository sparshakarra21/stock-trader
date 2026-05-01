package com.stocktrading.dto;

import com.stocktrading.model.Stock;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class StockDTO {
    private Long id;
    private String symbol;
    private String name;
    private BigDecimal price;
    private Integer availableShares;
    private Integer totalShares;
    private BigDecimal purchasePercentage;

    public static StockDTO from(Stock stock) {
        StockDTO dto = new StockDTO();
        dto.setId(stock.getId());
        dto.setSymbol(stock.getSymbol());
        dto.setName(stock.getName());
        dto.setPrice(stock.getPrice());
        dto.setAvailableShares(stock.getAvailableShares());
        dto.setTotalShares(stock.getTotalShares());
        dto.setPurchasePercentage(stock.getPurchasePercentage());
        return dto;
    }
}
