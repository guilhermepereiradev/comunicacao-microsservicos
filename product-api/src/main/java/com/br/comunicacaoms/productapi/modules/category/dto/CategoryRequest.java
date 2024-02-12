package com.br.comunicacaoms.productapi.modules.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(@NotBlank String description) {
}
