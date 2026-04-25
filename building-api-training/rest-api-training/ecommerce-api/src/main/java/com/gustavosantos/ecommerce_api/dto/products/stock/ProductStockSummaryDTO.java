package com.gustavosantos.ecommerce_api.dto.products.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockSummaryDTO {

    private UUID publicId;
    private Integer quantity;
}