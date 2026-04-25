package com.gustavosantos.ecommerce_api.dto.products.stock;

import com.gustavosantos.ecommerce_api.dto.products.stock.movements.StockMovementListDTO;
import com.gustavosantos.ecommerce_api.dto.products.ProductSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDetailDTO {

    private UUID publicId;
    private ProductSummaryDTO product;
    private Integer quantity;
    private List<StockMovementListDTO> movements;
}