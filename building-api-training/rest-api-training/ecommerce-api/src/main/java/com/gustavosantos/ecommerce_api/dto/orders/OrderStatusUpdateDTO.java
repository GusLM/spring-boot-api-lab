package com.gustavosantos.ecommerce_api.dto.orders;

import com.gustavosantos.ecommerce_api.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateDTO {

    private OrderStatus status;
}