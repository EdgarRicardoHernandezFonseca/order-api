package com.edgar.order.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edgar.order.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
}