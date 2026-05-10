package com.gustavosantos.ecommerce_api.services;

import com.gustavosantos.ecommerce_api.dto.products.categories.ProductCategoryListDTO;
import com.gustavosantos.ecommerce_api.repositories.ProductCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public Page<ProductCategoryListDTO> findProductsCategories(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (name == null || name.isBlank()) {
            return productCategoryRepository.findAllProjected(pageable);
        }

        return productCategoryRepository.findByName(name, pageable);
    }
}
