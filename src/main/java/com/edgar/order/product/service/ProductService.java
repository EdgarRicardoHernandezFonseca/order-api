package com.edgar.order.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.edgar.order.product.dto.CreateProductRequest;
import com.edgar.order.product.dto.ProductResponse;
import com.edgar.order.product.entity.Product;
import com.edgar.order.product.repository.ProductRepository;
import com.edgar.order.common.exception.ProductNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public ProductResponse create(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        return map(repository.save(product));
    }

    public Product getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public List<ProductResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    private ProductResponse map(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}