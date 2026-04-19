package com.gustavosantos.ecommerce_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID publicId;

    @Setter
    @Column(nullable = false, length = 45)
    private String name;

    @Setter
    @Column(nullable = false, length = 75)
    private String description;

    @Setter
    @ManyToOne
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory productCategory;

    @Setter
    @Column(name = "current_price", nullable = false, precision = 8, scale = 2)
    private BigDecimal currentPrice;

    public Product() {
    }

    public Product(String name, String description, ProductCategory productCategory, BigDecimal currentPrice) {
        this.name = name;
        this.description = description;
        this.productCategory = productCategory;
        this.currentPrice = currentPrice;
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
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(publicId, product.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicId);
    }
}
