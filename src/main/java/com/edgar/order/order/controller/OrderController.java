package com.edgar.order.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.order.order.dto.UpdateOrderStatusRequest;
import com.edgar.order.order.entity.Order;
import com.edgar.order.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	@PutMapping("/{id}/status")
	public ResponseEntity<Order> updateStatus(
	        @PathVariable Long id,
	        @RequestBody @Valid UpdateOrderStatusRequest request) {

	    Order updatedOrder = orderService.updateOrderStatus(id, request.getStatus());
	    return ResponseEntity.ok(updatedOrder);
	}

}
