package com.gustavosantos.ecommerce_api.dto.products.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategorySummaryDTO {

    private UUID publicId;
    private String name;
}