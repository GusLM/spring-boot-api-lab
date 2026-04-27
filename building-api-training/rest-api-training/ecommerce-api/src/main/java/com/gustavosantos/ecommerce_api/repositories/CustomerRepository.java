package com.gustavosantos.ecommerce_api.repositories;

import com.gustavosantos.ecommerce_api.dto.customers.CustomerDetailDTO;
import com.gustavosantos.ecommerce_api.dto.customers.CustomerListDTO;
import com.gustavosantos.ecommerce_api.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("""
            SELECT new com.gustavosantos.ecommerce_api.dto.customers.CustomerListDTO(
                c.publicId,
                c.firstName,
                c.lastName
            )
            FROM Customer c
            WHERE c.firstName LIKE %?1%
               OR c.lastName LIKE %?1%
            """)
    Page<CustomerListDTO> findByFirstNameOrLastName(String name, Pageable pageable);

    @Query("""
            SELECT new com.gustavosantos.ecommerce_api.dto.customers.CustomerListDTO(
                c.publicId,
                c.firstName,
                c.lastName
            )
            FROM Customer c
            """)
    Page<CustomerListDTO> findAllProjected(Pageable pageable);

    @Query("""
            SELECT new com.gustavosantos.ecommerce_api.dto.customers.CustomerDetailDTO(
                 c.publicId,
                 c.firstName,
                 c.lastName,
                 c.taxId,
                 c.email,
                 c.phone,
                 c.street,
                 c.number,
                 c.neighborhood,
                 c.city,
                 c.state,
                 c.postalCode,
                 c.country
            )
            FROM Customer c
            WHERE c.publicId = :publicId
            """)
    Optional<CustomerDetailDTO> findByPublicId(UUID publicId);
}
