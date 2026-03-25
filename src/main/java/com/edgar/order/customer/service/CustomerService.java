package com.edgar.order.customer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.edgar.order.common.exception.CustomerNotFoundException;
import com.edgar.order.customer.dto.CreateCustomerRequest;
import com.edgar.order.customer.dto.CustomerResponse;
import com.edgar.order.customer.entity.Customer;
import com.edgar.order.customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerResponse create(CreateCustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        return mapToResponse(repository.save(customer));
    }

    public CustomerResponse getById(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return mapToResponse(customer);
    }

    public List<CustomerResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }
}