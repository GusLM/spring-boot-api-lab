package com.gustavosantos.ecommerce_api.repositories;

import com.gustavosantos.ecommerce_api.dto.CustomerListDTO;
import com.gustavosantos.ecommerce_api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.firstName = ?1 OR c.lastName = ?1")
    List<Customer> findByFirstNameOrLastName(String name);
}
