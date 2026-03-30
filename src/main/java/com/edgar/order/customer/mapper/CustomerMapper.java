package com.edgar.order.customer.mapper;

import org.springframework.stereotype.Component;

import com.edgar.order.customer.dto.CustomerResponse;
import com.edgar.order.customer.entity.Customer;

@Component
public class CustomerMapper {

    public CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }
}