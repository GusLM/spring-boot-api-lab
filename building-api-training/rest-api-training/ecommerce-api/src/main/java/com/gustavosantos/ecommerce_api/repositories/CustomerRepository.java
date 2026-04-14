package com.gustavosantos.ecommerce_api.repositories;

import com.gustavosantos.ecommerce_api.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.firstName LIKE %?1% OR c.lastName LIKE %?1%")
    Page<Customer> findByFirstNameOrLastName(String name, Pageable pageable);
}
