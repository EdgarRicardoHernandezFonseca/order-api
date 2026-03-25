package com.edgar.order.order.dto;

import com.edgar.order.customer.dto.CustomerResponse;
import com.edgar.order.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long id;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private BigDecimal totalAmount;

    private CustomerResponse customer;
    private List<OrderItemResponse> items;
}