package com.gustavosantos.ecommerce_api.model;

import com.gustavosantos.ecommerce_api.model.enums.StockMovementType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_product_id", nullable = false)
    private ProductStock productStock;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = true)
    private OrderItem orderItem;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private StockMovementType movementType;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public StockMovement() {
    }

    public StockMovement(ProductStock productStock, OrderItem orderItem, StockMovementType movementType, Integer quantity) {
        this.productStock = productStock;
        this.orderItem = orderItem;
        this.movementType = movementType;
        this.createdAt = Instant.now();
        this.quantity = quantity;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockMovement that = (StockMovement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
