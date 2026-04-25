package com.gustavosantos.ecommerce_api.dto.products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {

    private String name;
    private String description;
    private BigDecimal currentPrice;
    private UUID categoryPublicId;
    private Integer initialStockQuantity;
}