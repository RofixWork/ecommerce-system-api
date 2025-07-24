package com.rofix.ecommerce_system.repository;

import com.rofix.ecommerce_system.entity.Cart;
import com.rofix.ecommerce_system.entity.CartItem;
import com.rofix.ecommerce_system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
