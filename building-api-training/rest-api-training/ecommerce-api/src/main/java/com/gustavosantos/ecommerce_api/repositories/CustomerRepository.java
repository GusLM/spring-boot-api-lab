package com.gustavosantos.ecommerce_api.repositories;

import com.gustavosantos.ecommerce_api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
