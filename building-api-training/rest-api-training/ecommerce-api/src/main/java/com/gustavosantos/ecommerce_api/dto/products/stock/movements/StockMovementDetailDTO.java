package com.gustavosantos.ecommerce_api.dto.products.stock.movements;

import com.gustavosantos.ecommerce_api.dto.products.ProductSummaryDTO;
import com.gustavosantos.ecommerce_api.model.enums.StockMovementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementDetailDTO {

    private UUID publicId;
    private ProductSummaryDTO product;
    private UUID orderItemPublicId;
    private StockMovementType movementType;
    private Integer quantity;
    private Instant createdAt;
}