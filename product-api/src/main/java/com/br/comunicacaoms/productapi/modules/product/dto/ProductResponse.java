package com.br.comunicacaoms.productapi.modules.product.dto;


import com.br.comunicacaoms.productapi.modules.category.dto.CategoryResponse;
import com.br.comunicacaoms.productapi.modules.product.model.Product;
import com.br.comunicacaoms.productapi.modules.supplier.dto.SupplierResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ProductResponse(
        Integer id,
        String name,
        SupplierResponse supplier,
        CategoryResponse category,
        Integer quantityAvailable,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createAt
) {

    public ProductResponse(Product product) {
        this(product.getId(),
                product.getName(),
                new SupplierResponse(product.getSupplier()),
                new CategoryResponse(product.getCategory()),
                product.getQuantityAvailable(),
                product.getCreatedAt());
    }
}