package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.request.CartRequestDTO;
import com.rofix.ecommerce_system.dto.response.CartItemResponseDTO;
import com.rofix.ecommerce_system.dto.response.CartResponseDTO;
import com.rofix.ecommerce_system.dto.response.CategoryResponseDTO;
import com.rofix.ecommerce_system.dto.response.ProductImageResponseDTO;
import com.rofix.ecommerce_system.entity.Cart;
import com.rofix.ecommerce_system.entity.CartItem;
import com.rofix.ecommerce_system.entity.Product;
import com.rofix.ecommerce_system.entity.User;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.repository.CartItemRepository;
import com.rofix.ecommerce_system.repository.CartRepository;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.utils.EntityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    public CartResponseDTO addProductFromCart(CartRequestDTO cartRequestDTO, UserDetailsImpl userDetails) {

        Cart cart = getCartOrCreate(userDetails.getUser());
        Product product = entityHelper.getProductOrThrow(cartRequestDTO.getProductId());

        isPurchasable(cartRequestDTO.getQuantity(), product);

        //check if item already exist in cart (add quantity)
        Optional<CartItem> alreadyExistInCart = cartItemRepository.findByCartAndProduct(cart, product);

        if (alreadyExistInCart.isPresent()) {
            CartItem cartItem = alreadyExistInCart.get();

            Integer newTotalQuantity = cartItem.getQuantity() + cartRequestDTO.getQuantity();

            isPurchasable(newTotalQuantity, product);

            cartItem.setQuantity(newTotalQuantity);
            cartItemRepository.save(cartItem);
            log.info("Cart item has been updated successfully");
            return getCartResponseDTO(cart, product);
        }

        //create cart Item
        CartItem newCartItem = new CartItem(cart, product, cartRequestDTO.getQuantity()),
                savedCartItem = cartItemRepository.save(newCartItem);
        log.info("Saved CartItem: {}", savedCartItem);

        //add item to cart
        cart.getCartItems().add(savedCartItem);
        cart = cartRepository.save(cart);
        log.info("Added CartItem {} to Cart: {}", savedCartItem.getId(), cart.getId());

        return getCartResponseDTO(cart, product);
    }

    //    ===================================== HELPERS =====================================
    private CartResponseDTO getCartResponseDTO(Cart cart, Product product) {
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

                    CategoryResponseDTO categoryResponseDTO = modelMapper.map(product.getCategory(), CategoryResponseDTO.class);
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

    private Cart getCartOrCreate(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart(user);
            return cartRepository.save(cart);
        });
    }

    private void isPurchasable(Integer quantity, Product product) {
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
