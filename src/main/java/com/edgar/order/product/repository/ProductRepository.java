package com.edgar.order.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edgar.order.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
