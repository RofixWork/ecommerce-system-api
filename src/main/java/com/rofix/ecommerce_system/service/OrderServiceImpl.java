package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.config.AppConstants;
import com.rofix.ecommerce_system.dto.response.OrderResponseDTO;
import com.rofix.ecommerce_system.entity.*;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.repository.OrderRepository;
import com.rofix.ecommerce_system.response.PageListResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.utils.CartHelper;
import com.rofix.ecommerce_system.utils.EntityHelper;
import com.rofix.ecommerce_system.utils.OrderHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final CartHelper cartHelper;
    private final CartService cartService;
    private final OrderHelper orderHelper;
    private final EntityHelper entityHelper;
    private final OrderRepository orderRepository;

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

    @Override
    public OrderResponseDTO getOrderDetails(Long orderId, UserDetailsImpl userDetails) {
        return orderHelper.getOrderResponseDTO(entityHelper.getOrderOrThrow(orderId, userDetails));
    }

    @Override
    public PageListResponse<OrderResponseDTO> getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, UserDetailsImpl userDetails) {
        entityHelper.checkOrderField(sortBy, AppConstants.ALLOWED_ORDER_ORDERING_FIELDS);
        Pageable pageable = entityHelper.getPageable(pageNumber, pageSize, sortBy, sortOrder);
        User user = userDetails.getUser();
        Page<Order> orderPage = orderRepository.findAllByUser(user, pageable);
        List<Order> orders = orderPage.getContent();
        List<OrderResponseDTO> orderResponseDTOS = orders.stream().map(orderHelper::getOrderResponseDTO).toList();

        boolean isEmpty = orderPage.isEmpty();

        if (isEmpty) {
            return new PageListResponse<>(List.of(), 0, 0, 0, 0L, true);
        }

        return new PageListResponse<>(
                orderResponseDTOS,
                orderPage.getSize(),
                orderPage.getNumber() + 1,
                orderPage.getTotalPages(),
                orderPage.getTotalElements(),
                orderPage.isLast()
        );
    }
}
