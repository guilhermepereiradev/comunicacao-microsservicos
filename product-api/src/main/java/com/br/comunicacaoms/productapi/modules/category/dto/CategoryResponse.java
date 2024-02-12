package com.br.comunicacaoms.productapi.modules.category.dto;


import com.br.comunicacaoms.productapi.modules.category.model.Category;

public record CategoryResponse(Integer id, String description) {
    public CategoryResponse(Category category) {
        this(category.getId(), category.getDescription());
    }
}