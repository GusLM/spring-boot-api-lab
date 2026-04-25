package com.gustavosantos.ecommerce_api.dto.orders;

import com.gustavosantos.ecommerce_api.dto.customers.CustomerSummaryDTO;
import com.gustavosantos.ecommerce_api.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDTO {

    private UUID publicId;
    private CustomerSummaryDTO customer;
    private OrderStatus status;
    private BigDecimal total;
    private Instant createdAt;
}