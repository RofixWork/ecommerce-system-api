package com.rofix.ecommerce_system.entity;

import com.rofix.ecommerce_system.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cartItems")
@Data
@NoArgsConstructor
public class CartItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Check(constraints = "quantity >= 1")
    @Column(nullable = false)
    private Integer quantity;

    public CartItem(Product product, Cart cart, Integer quantity) {
        this.product = product;
        this.cart = cart;
        this.quantity = quantity;
    }
}
