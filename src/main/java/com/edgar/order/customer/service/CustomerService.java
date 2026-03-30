package com.edgar.order.customer.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.edgar.order.common.exception.CustomerNotFoundException;
import com.edgar.order.customer.dto.CreateCustomerRequest;
import com.edgar.order.customer.dto.CustomerResponse;
import com.edgar.order.customer.entity.Customer;
import com.edgar.order.customer.repository.CustomerRepository;
import com.edgar.order.customer.repository.CustomerSpecification;

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

    public Page<CustomerResponse> getAll(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(this::mapToResponse);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }
    
    public Page<CustomerResponse> search(String name, Pageable pageable) {

        Page<Customer> result;

        if (name != null && !name.isEmpty()) {
            result = repository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            result = repository.findAll(pageable);
        }

        return result.map(this::mapToResponse);
    }
    
    public Page<CustomerResponse> search(String name, String email, Pageable pageable) {

        Specification<Customer> spec = Specification
                .where(CustomerSpecification.hasName(name))
                .and(CustomerSpecification.hasEmail(email));

        return repository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }
}