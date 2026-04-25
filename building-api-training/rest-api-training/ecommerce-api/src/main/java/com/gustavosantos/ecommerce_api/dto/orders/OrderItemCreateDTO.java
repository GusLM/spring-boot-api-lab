package com.gustavosantos.ecommerce_api.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCreateDTO {

    private UUID productPublicId;
    private Integer quantity;
}