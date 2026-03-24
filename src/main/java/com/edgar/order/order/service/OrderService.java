package com.edgar.order.order.service;

import com.edgar.order.common.exception.InsufficientStockException;
import com.edgar.order.common.exception.ProductNotFoundException;
import com.edgar.order.customer.entity.Customer;
import com.edgar.order.customer.repository.CustomerRepository;
import com.edgar.order.order.dto.CreateOrderRequest;
import com.edgar.order.order.dto.OrderItemRequest;
import com.edgar.order.order.dto.OrderResponse;
import com.edgar.order.order.entity.*;
import com.edgar.order.order.mapper.OrderMapper;
import com.edgar.order.order.repository.OrderRepository;
import com.edgar.order.product.entity.Product;
import com.edgar.order.product.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        // 🔹 1. Validar cliente
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 🔹 2. Crear orden base
        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        // 🔹 3. Procesar items
        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));

            // 🔴 Validar stock
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(
                	    "Insufficient stock for product: " + product.getName()
                		);
            }

            // 🔹 Crear item
            BigDecimal price = product.getPrice();
            int quantity = itemRequest.getQuantity();

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .price(price)
                    .build();

            // 🔗 Relación bidireccional
            order.addItem(item);

            // 🔹 Calcular total
            total = total.add(price.multiply(BigDecimal.valueOf(quantity)));

            // 🔹 Actualizar stock
            product.setStock(product.getStock() - quantity);
        }

        // 🔹 4. Set total final
        order.setTotalAmount(total);

        // 🔹 5. Guardar orden (cascade guarda items)
        return OrderMapper.toResponse(order);
    }
    
    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {

        // 🔹 1. Buscar orden
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus currentStatus = order.getStatus();

        // 🔹 2. Validar transición
        validateStatusTransition(currentStatus, newStatus);

        // 🔹 3. Lógica adicional según estado
        if (newStatus == OrderStatus.CANCELLED) {
            restoreStock(order);
        }

        // 🔹 4. Actualizar estado
        order.setStatus(newStatus);

        return OrderMapper.toResponse(order);
    }
    
    private void validateStatusTransition(OrderStatus current, OrderStatus next) {

        if (current == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot modify a cancelled order");
        }

        if (current == OrderStatus.SHIPPED) {
            throw new RuntimeException("Cannot modify a shipped order");
        }

        if (current == OrderStatus.CREATED && next == OrderStatus.SHIPPED) {
            throw new RuntimeException("Order must be paid before shipping");
        }

        if (current == OrderStatus.PAID && next == OrderStatus.CREATED) {
            throw new RuntimeException("Cannot revert a paid order");
        }
    }
    
    private void restoreStock(Order order) {

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }
    }
    
    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case CREATED -> next == OrderStatus.PAID || next == OrderStatus.CANCELLED;
            case PAID -> next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;
            default -> false;
        };
    }
}