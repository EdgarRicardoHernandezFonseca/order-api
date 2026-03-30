package com.edgar.order.customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.edgar.order.customer.entity.Customer;

public interface CustomerRepository extends 
		JpaRepository<Customer, Long>,
		JpaSpecificationExecutor<Customer> {
	
	Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Customer> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Customer> findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(
            String name,
            String email,
            Pageable pageable
    );
}
