package com.br.comunicacaoms.productapi.dtos;


import com.br.comunicacaoms.productapi.model.Category;

public record CategoryResponse(Integer id, String description) {
    public CategoryResponse(Category category) {
        this(category.getId(), category.getDescription());
    }
}