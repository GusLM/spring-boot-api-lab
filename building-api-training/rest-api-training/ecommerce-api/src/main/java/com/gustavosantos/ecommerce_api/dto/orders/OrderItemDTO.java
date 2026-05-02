package com.gustavosantos.ecommerce_api.dto.orders;

import com.gustavosantos.ecommerce_api.dto.products.ProductSummaryDTO;
import com.gustavosantos.ecommerce_api.model.Order;
import com.gustavosantos.ecommerce_api.model.Product;
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
public class OrderItemDTO {

    private UUID publicId;
    private ProductSummaryDTO product;
    private Integer quantity;
    private BigDecimal value;
    private BigDecimal subTotal;
}
