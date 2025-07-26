package com.rofix.ecommerce_system.service;

import com.rofix.ecommerce_system.dto.response.CategoryResponseDTO;
import com.rofix.ecommerce_system.dto.response.OrderItemResponseDTO;
import com.rofix.ecommerce_system.dto.response.OrderResponseDTO;
import com.rofix.ecommerce_system.dto.response.ProductImageResponseDTO;
import com.rofix.ecommerce_system.entity.*;
import com.rofix.ecommerce_system.enums.OrderStatus;
import com.rofix.ecommerce_system.exception.base.BadRequestException;
import com.rofix.ecommerce_system.repository.OrderRepository;
import com.rofix.ecommerce_system.response.APIResponse;
import com.rofix.ecommerce_system.security.service.UserDetailsImpl;
import com.rofix.ecommerce_system.utils.CartHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final CartService cartService;

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
        BigDecimal totalPrice = getTotalPrice(cartItems);

        // create order
        Order newOrder = getOrder(user, totalPrice, cartItems);

        //clear cart
        cartService.clearCart(userDetails);

        //RETURN ORDER
        return getOrderResponseDTO(newOrder);
    }

    //    ============================ HELPERS ==========================
    private OrderResponseDTO getOrderResponseDTO(Order newOrder) {
        OrderResponseDTO orderResponseDTO = modelMapper.map(newOrder, OrderResponseDTO.class);
        List<OrderItemResponseDTO> orderItemResponseDTOS = newOrder.getOrderItems().stream()
                .map(orderItem -> {
                    Product product = orderItem.getProduct();
                    List<ProductImage> productImages = product.getProductImages();
                    OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
                    orderItemResponseDTO.setId(orderItem.getId());
                    orderItemResponseDTO.setName(product.getName());
                    orderItemResponseDTO.setSlug(product.getSlug());
                    orderItemResponseDTO.setQuantity(orderItem.getQuantity());
                    orderItemResponseDTO.setPrice(product.getPrice());
                    orderItemResponseDTO.setCategory(modelMapper.map(product.getCategory(), CategoryResponseDTO.class));

                    List<ProductImageResponseDTO> productImageResponseDTOS = productImages.stream().map(image -> modelMapper.map(image, ProductImageResponseDTO.class)).toList();
                    orderItemResponseDTO.setImages(productImageResponseDTOS);

                    return orderItemResponseDTO;
                }).toList();
        orderResponseDTO.setOrders(orderItemResponseDTOS);
        return orderResponseDTO;
    }

    private Order getOrder(User user, BigDecimal totalPrice, List<CartItem> cartItems) {
        Order order = new Order(user, totalPrice);

        //CREATE ORDER ITEM
        List<OrderItem> orderItems = cartItems.stream().map(item -> new OrderItem(
                item.getQuantity(),
                item.getProduct().getPrice(),
                order,
                item.getProduct()
        )).toList();

        order.setOrderItems(orderItems);

        //SAVE ORDER
        Order newOrder = orderRepository.save(order);
        log.info("Order items set for Order ID: {} successfully.", newOrder.getId());
        log.info("Order {} created for user {}", newOrder.getId(), user.getUsername());
        return newOrder;
    }

    private static BigDecimal getTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream().reduce(BigDecimal.ZERO, (acc, item) -> acc.add(item.getItemTotal()), BigDecimal::add);
    }
}
