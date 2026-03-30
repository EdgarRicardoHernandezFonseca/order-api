package com.edgar.order.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.edgar.order.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
