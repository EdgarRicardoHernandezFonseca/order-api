package com.edgar.order.order.service;

import com.edgar.order.customer.entity.Customer;
import com.edgar.order.customer.repository.CustomerRepository;
import com.edgar.order.order.dto.CreateOrderRequest;
import com.edgar.order.order.dto.OrderItemRequest;
import com.edgar.order.order.entity.*;
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
    public Order createOrder(CreateOrderRequest request) {

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
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 🔴 Validar stock
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
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
        return orderRepository.save(order);
    }
}