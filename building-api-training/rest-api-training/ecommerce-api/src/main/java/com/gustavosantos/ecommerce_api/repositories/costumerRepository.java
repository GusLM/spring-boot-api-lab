package com.gustavosantos.ecommerce_api.repositories;

import com.gustavosantos.ecommerce_api.model.costumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface costumerRepository extends JpaRepository<costumer, Long> {
}
