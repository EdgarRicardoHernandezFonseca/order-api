package com.edgar.order.order.mapper;

import com.edgar.order.customer.dto.CustomerResponse;
import com.edgar.order.customer.entity.Customer;
import com.edgar.order.order.dto.*;
import com.edgar.order.order.entity.Order;
import com.edgar.order.order.entity.OrderItem;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .customer(toCustomer(order.getCustomer()))
                .items(toItems(order.getItems()))
                .build();
    }

    private CustomerResponse toCustomer(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }

    private List<OrderItemResponse> toItems(List<OrderItem> items) {
        return items.stream()
                .map(OrderMapper::toItem)
                .toList();
    }

    private OrderItemResponse toItem(OrderItem item) {
        return OrderItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }
}