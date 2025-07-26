package com.rofix.ecommerce_system.repository;

import com.rofix.ecommerce_system.entity.Cart;
import com.rofix.ecommerce_system.entity.CartItem;
import com.rofix.ecommerce_system.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct_Id(Cart cart, Long productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem as ci WHERE ci.cart = :cart")
    void deleteAllByCart(@Param("cart") Cart cart);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem as ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId")
    void deleteByCartAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);
}
