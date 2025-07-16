package com.rofix.ecommerce_system.entity;

import com.github.slugify.Slugify;
import com.rofix.ecommerce_system.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Check;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@ToString(exclude = {"productImages", "cartItems"})
public class Product extends BaseEntity {

    private static final Slugify SLUGIFY;

    static {
        SLUGIFY = Slugify.builder().lowerCase(true).build();
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, updatable = false, unique = true)
    private String slug;

    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    @Check(constraints = "stock > 0")
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
    private List<CartItem> cartItems = new ArrayList<>();

    public Product(String name, String description, BigDecimal price, Integer stock, User createdBy, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.createdBy = createdBy;
        this.category = category;
    }

    public Product(String name, BigDecimal price, Integer stock, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    private void generateSlug() {
        if (StringUtils.hasText(this.name)) {
            this.slug = SLUGIFY.slugify(this.name);
        }
    }

    @PrePersist
    private void prePersist() {
        generateSlug();
    }

    @PreUpdate
    private void preUpdate() {
        generateSlug();
    }
}
