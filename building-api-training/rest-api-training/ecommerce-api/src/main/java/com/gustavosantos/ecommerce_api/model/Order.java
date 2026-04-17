package com.gustavosantos.ecommerce_api.model;

import com.gustavosantos.ecommerce_api.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    public Order() {
    }

    public Order(Customer customer, OrderStatus status, Date createdAt) {
        this.customer = customer;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(publicId, order.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicId);
    }
}
