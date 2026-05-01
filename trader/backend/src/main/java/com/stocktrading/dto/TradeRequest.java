package com.stocktrading.dto;

import lombok.Data;

@Data
public class TradeRequest {
    private Long stockId;
    private Integer quantity;
}
