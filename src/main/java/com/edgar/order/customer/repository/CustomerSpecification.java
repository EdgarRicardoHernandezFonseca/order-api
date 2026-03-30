package com.edgar.order.customer.repository;

import org.springframework.data.jpa.domain.Specification;

import com.edgar.order.customer.entity.Customer;

public class CustomerSpecification {

    public static Specification<Customer> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null :
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Customer> hasEmail(String email) {
        return (root, query, cb) ->
                email == null ? null :
                        cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }
}