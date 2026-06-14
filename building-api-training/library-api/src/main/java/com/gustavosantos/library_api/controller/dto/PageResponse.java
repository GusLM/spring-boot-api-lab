package com.gustavosantos.library_api.controller.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T> (
        int page,
        int size,
        long totalElements,
        long totalPages,
        boolean first,
        boolean last,
        boolean empty,
        List<T> content
) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty(),
                page.getContent()
        );
    }
}
