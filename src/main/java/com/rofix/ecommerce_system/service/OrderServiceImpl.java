package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.response.OrderResponseDTO;
import com.rofix.ecommerce_system.entity.*;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.utils.CartHelper;
import com.rofix.ecommerce_system.utils.OrderHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final CartHelper cartHelper;
    private final CartService cartService;
    private final OrderHelper orderHelper;

    @Transactional
    @Override
    public OrderResponseDTO createOrder(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        //get user cart
        Cart cart = cartHelper.getCartOrThrow(user);
        //get items
        List<CartItem> cartItems = cart.getCartItems();
        //check cart has items or not
        String message = cartHelper.getEmptyCartMessageIfApplicable(cartItems);
        if (message != null) {
            throw new BadRequestException(message);
        }
        //calc total price
        BigDecimal totalPrice = orderHelper.getTotalPrice(cartItems);

        // create order
        Order newOrder = orderHelper.getOrder(user, totalPrice, cartItems);

        //clear cart
        cartService.clearCart(userDetails);

        //RETURN ORDER
        return orderHelper.getOrderResponseDTO(newOrder);
    }
}
