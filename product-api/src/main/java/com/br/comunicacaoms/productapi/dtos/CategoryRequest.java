package com.br.comunicacaoms.productapi.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(@NotBlank String description) {
}
