package com.edgar.order.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edgar.order.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
