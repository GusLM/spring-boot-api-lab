package com.gustavosantos.ecommerce_api.controllers;

import com.gustavosantos.ecommerce_api.dto.PageResponse;
import com.gustavosantos.ecommerce_api.dto.products.categories.ProductCategoryListDTO;
import com.gustavosantos.ecommerce_api.services.ProductCategoryService;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/product-categories")
@CrossOrigin(origins = "*")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductCategoryListDTO>> findProductsCategories(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be zero or greater") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be greater than zero") int size
    ) {
        Page<ProductCategoryListDTO> productCategories = productCategoryService.findProductsCategories(name, page, size);
        return ResponseEntity.ok().body(PageResponse.from(productCategories));
    }
}
