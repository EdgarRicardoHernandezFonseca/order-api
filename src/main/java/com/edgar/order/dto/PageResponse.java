package com.edgar.order.dto;

import java.util.List;

import lombok.Builder;

@Builder
public class PageResponse<T> {
    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}