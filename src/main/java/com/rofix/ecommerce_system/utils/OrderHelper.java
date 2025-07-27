package com.rofix.ecommerce_system.utils;

import com.rofix.ecommerce_system.dto.response.CategoryResponseDTO;
import com.rofix.ecommerce_system.dto.response.OrderItemResponseDTO;
import com.rofix.ecommerce_system.dto.response.OrderResponseDTO;
import com.rofix.ecommerce_system.dto.response.ProductImageResponseDTO;
import com.rofix.ecommerce_system.entity.*;
import com.rofix.ecommerce_system.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderHelper {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderResponseDTO getOrderResponseDTO(Order newOrder) {
        OrderResponseDTO orderResponseDTO = modelMapper.map(newOrder, OrderResponseDTO.class);
        List<OrderItemResponseDTO> orderItemResponseDTOS = newOrder.getOrderItems().stream()
                .map(this::getItemResponseDTO).toList();
        orderResponseDTO.setItems(orderItemResponseDTOS);
        return orderResponseDTO;
    }

    public OrderItemResponseDTO getItemResponseDTO(OrderItem orderItem) {
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
    }

    public Order getOrder(User user, BigDecimal totalPrice, List<CartItem> cartItems) {
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

    public BigDecimal getTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream().reduce(BigDecimal.ZERO, (acc, item) -> acc.add(item.getItemTotal()), BigDecimal::add);
    }
}
