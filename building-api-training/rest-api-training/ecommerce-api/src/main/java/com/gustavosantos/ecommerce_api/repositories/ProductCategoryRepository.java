package com.gustavosantos.ecommerce_api.repositories;

import com.gustavosantos.ecommerce_api.dto.products.categories.ProductCategoryListDTO;
import com.gustavosantos.ecommerce_api.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Query("""
            SELECT new com.gustavosantos.ecommerce_api.dto.products.categories.ProductCategoryListDTO(pc.publicId, pc.name)
            FROM ProductCategory pc
            WHERE pc.name LIKE %:name%
            """)
    Page<ProductCategoryListDTO> findByName(String name, Pageable pageable);

    @Query("""
            SELECT new com.gustavosantos.ecommerce_api.dto.products.categories.ProductCategoryListDTO(pc.publicId, pc.name)
            FROM ProductCategory pc
            """)
    Page<ProductCategoryListDTO> findAllProjected(Pageable pageable);

    boolean existsByName(String name);



}
