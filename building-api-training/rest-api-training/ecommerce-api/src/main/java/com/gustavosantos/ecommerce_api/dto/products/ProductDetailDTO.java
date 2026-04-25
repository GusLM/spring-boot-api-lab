package com.gustavosantos.ecommerce_api.dto.products;

import com.gustavosantos.ecommerce_api.dto.products.stock.ProductStockSummaryDTO;
import com.gustavosantos.ecommerce_api.dto.products.categories.ProductCategorySummaryDTO;
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
public class ProductDetailDTO {

    private UUID publicId;
    private String name;
    private String description;
    private BigDecimal currentPrice;
    private ProductCategorySummaryDTO category;
    private ProductStockSummaryDTO stock;
}