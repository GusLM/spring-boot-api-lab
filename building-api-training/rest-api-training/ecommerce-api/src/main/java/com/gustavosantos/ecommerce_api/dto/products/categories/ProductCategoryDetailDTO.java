package com.gustavosantos.ecommerce_api.dto.products.categories;

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
public class ProductCategoryDetailDTO {

    private UUID publicId;
    private String name;
    private List<ProductSummaryDTO> products;
}