package com.edgar.order.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.order.order.dto.CreateOrderRequest;
import com.edgar.order.order.dto.OrderResponse;
import com.edgar.order.order.dto.UpdateOrderStatusRequest;
import com.edgar.order.order.entity.Order;
import com.edgar.order.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	@PostMapping
	public ResponseEntity<OrderResponse> createOrder(
	        @RequestBody @Valid CreateOrderRequest request) {
		
		String username = authentication.getName();

	    return ResponseEntity.ok(orderService.createOrder(request));
	}
	
	@PutMapping("/{id}/status")
	public ResponseEntity<OrderResponse> updateStatus(
	        @PathVariable Long id,
	        @RequestBody @Valid UpdateOrderStatusRequest request) {

	    return ResponseEntity.ok(
	            orderService.updateOrderStatus(id, request.getStatus())
	    );
	}

}
