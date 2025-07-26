package com.rofix.ecommerce_system.utils;

import com.rofix.ecommerce_system.dto.response.CartItemResponseDTO;
import com.rofix.ecommerce_system.dto.response.CartResponseDTO;
import com.rofix.ecommerce_system.dto.response.CategoryResponseDTO;
import com.rofix.ecommerce_system.dto.response.ProductImageResponseDTO;
import com.rofix.ecommerce_system.entity.Cart;
import com.rofix.ecommerce_system.entity.CartItem;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.entity.User;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.exception.base.NotFoundException;
import com.rofix.ecommerce_system.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CartHelper {
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;

    public CartHelper(ModelMapper modelMapper, CartRepository cartRepository) {
        this.modelMapper = modelMapper;
        this.cartRepository = cartRepository;
    }

    public String getEmptyCartMessageIfApplicable(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            log.info("User cart is empty or not yet initialized.");
            return "Your cart is currently empty. Start adding some products!";
        }
        return null;
    }

    public CartResponseDTO getCartResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = modelMapper.map(cart, CartResponseDTO.class);
        List<CartItemResponseDTO> cartItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    Product cartItemProduct = cartItem.getProduct();
                    CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
                    cartItemResponseDTO.setId(cartItem.getId());
                    cartItemResponseDTO.setName(cartItemProduct.getName());
                    cartItemResponseDTO.setSlug(cartItemProduct.getSlug());
                    cartItemResponseDTO.setPrice(cartItemProduct.getPrice());
                    cartItemResponseDTO.setQuantity(cartItem.getQuantity());
                    cartItemResponseDTO.setItemTotal(cartItem.getItemTotal());

                    CategoryResponseDTO categoryResponseDTO = modelMapper.map(cartItemProduct.getCategory(), CategoryResponseDTO.class);
                    List<ProductImageResponseDTO> productImageResponseDTO = cartItemProduct.getProductImages()
                            .stream().map(image -> modelMapper.map(image, ProductImageResponseDTO.class))
                            .toList();

                    cartItemResponseDTO.setCategory(categoryResponseDTO);
                    cartItemResponseDTO.setImages(productImageResponseDTO);

                    return cartItemResponseDTO;
                })
                .toList();
        cartResponseDTO.setCartItems(cartItems);
        return cartResponseDTO;
    }

    public Cart getCartOrCreate(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart(user);
            return cartRepository.save(cart);
        });
    }

    public Cart getCartOrThrow(User user) {
        return cartRepository.findByUser(user).orElseThrow(
                () -> {
                    log.info("Cart with id {} not found", user.getId());
                    return new NotFoundException("You dont have any Cart!!!");
                }
        );
    }

    public void isPurchasable(Integer quantity, Product product) {
        if (product.getStock() <= 0) {
            log.warn("Attempt to access or add out-of-stock Product ID: {} ({}).",
                    product.getId(), product.getName());
            throw new BadRequestException("Product '" + product.getName() + "' is currently out of stock.");
        }

        if (quantity > product.getStock()) {
            log.warn("Cart add failed: User requested {} units of Product ID {} but only {} are in stock.",
                    quantity, product.getId(), product.getStock());

            throw new BadRequestException("Requested quantity (" + quantity +
                    ") exceeds available stock for '" + product.getName() +
                    "'. Only " + product.getStock() + " left.");
        }
    }
}
