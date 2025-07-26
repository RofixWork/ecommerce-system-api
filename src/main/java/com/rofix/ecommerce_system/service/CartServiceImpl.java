package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.CartRequestDTO;
import com.rofix.ecommerce_system.dto.response.CartResponseDTO;
import com.rofix.ecommerce_system.entity.Cart;
import com.rofix.ecommerce_system.entity.CartItem;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.repository.CartItemRepository;
import com.rofix.ecommerce_system.repository.CartRepository;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.utils.CartHelper;
import com.rofix.ecommerce_system.utils.EntityHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final EntityHelper entityHelper;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartHelper cartHelper;

    @Transactional
    @Override
    public CartResponseDTO addProductFromCart(CartRequestDTO cartRequestDTO, UserDetailsImpl userDetails) {

        Cart cart = cartHelper.getCartOrCreate(userDetails.getUser());
        Product product = entityHelper.getProductOrThrow(cartRequestDTO.getProductId());

        cartHelper.isPurchasable(cartRequestDTO.getQuantity(), product);

        //check if item already exist in cart (add quantity)
        Optional<CartItem> alreadyExistInCart = cartItemRepository.findByCartAndProduct_Id(cart, product.getId());

        if (alreadyExistInCart.isPresent()) {
            CartItem cartItem = alreadyExistInCart.get();

            Integer newTotalQuantity = cartItem.getQuantity() + cartRequestDTO.getQuantity();

            cartHelper.isPurchasable(newTotalQuantity, product);

            cartItem.setQuantity(newTotalQuantity);
            cartItemRepository.save(cartItem);
            log.info("Cart item has been updated successfully");
            return cartHelper.getCartResponseDTO(cart);
        }

        //create cart Item
        CartItem newCartItem = new CartItem(cart, product, cartRequestDTO.getQuantity()),
                savedCartItem = cartItemRepository.save(newCartItem);
        log.info("Saved CartItem: {}", savedCartItem);

        //add item to cart
        cart.getCartItems().add(savedCartItem);
        cart = cartRepository.save(cart);
        log.info("Added CartItem {} to Cart: {}", savedCartItem.getId(), cart.getId());

        return cartHelper.getCartResponseDTO(cart);
    }

    @Override
    public CartResponseDTO getCartItems(UserDetailsImpl userDetails) {
        Cart cart = cartHelper.getCartOrThrow(userDetails.getUser());
        return cartHelper.getCartResponseDTO(cart);
    }

    @Transactional
    @Override
    public String clearCart(UserDetailsImpl userDetails) {
        Cart cart = cartHelper.getCartOrThrow(userDetails.getUser());

        List<CartItem> cartItems = cart.getCartItems();
        String message = cartHelper.getEmptyCartMessageIfApplicable(cartItems);
        if (message != null) return message;

        cartItemRepository.deleteAllByCart(cart);

        log.info("Successfully cleared cart for User ID: {}. Deleted {} items.", userDetails.getId(), cartItems.size());
        return "Cart items has been deleted successfully";
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long productId, UserDetailsImpl userDetails) {
        Cart cart = cartHelper.getCartOrThrow(userDetails.getUser());

        List<CartItem> cartItems = cart.getCartItems();
        String message = cartHelper.getEmptyCartMessageIfApplicable(cartItems);
        if (message != null) return message;

        cartItemRepository.findByCartAndProduct_Id(cart, productId).orElseThrow(() -> {
            log.warn("Product {} not found in cart of user {}", productId, userDetails.getUsername());
            return new NotFoundException("This product is not found in your cart.");
        });

        cartItemRepository.deleteByCartAndProductId(cart.getId(), productId);
        return "Product successfully removed from the cart.";
    }
}
