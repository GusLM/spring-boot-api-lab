package com.gustavosantos.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@Table(name = "stock_products")
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID publicId;

    @Setter
    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Setter
    @Column(nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "productStock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockMovement> movements;

    public ProductStock() {
    }

    public ProductStock(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void addStockMovement(StockMovement stockMovement) {
        movements.add(stockMovement);
    }

    public void removeStockMovement(StockMovement stockMovement) {
        movements.remove(stockMovement);
    }

    @PrePersist
    public void generateUuid() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductStock that = (ProductStock) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}