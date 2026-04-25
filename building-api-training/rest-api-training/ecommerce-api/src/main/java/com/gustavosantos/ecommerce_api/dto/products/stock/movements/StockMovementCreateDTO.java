package com.gustavosantos.ecommerce_api.dto.products.stock.movements;

import com.gustavosantos.ecommerce_api.model.enums.StockMovementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementCreateDTO {

    private UUID productPublicId;
    private StockMovementType movementType;
    private Integer quantity;
}