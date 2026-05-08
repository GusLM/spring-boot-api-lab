package com.gustavosantos.ecommerce_api.repositories;

import com.gustavosantos.ecommerce_api.dto.orders.OrderListDTO;
import com.gustavosantos.ecommerce_api.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
            SELECT new com.gustavosantos.ecommerce_api.dto.orders.OrderListDTO(
                        o.publicId,
                        o.customer.publicId,
                        o.status,
                        SUM(i.value * i.quantity),
                        o.createdAt
            )
            FROM Order o
            JOIN o.items i
            WHERE o.customer.publicId = :publicId
            GROUP BY o.id, o.publicId, o.customer.publicId, o.status, o.createdAt
            """)
    Page<OrderListDTO> findByCustomer(UUID publicId, Pageable pageable);
}
