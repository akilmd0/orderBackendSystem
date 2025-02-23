package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderRequest {
    private UUID userId;
    private List<UUID> itemIds;
    private BigDecimal totalAmount;
}
