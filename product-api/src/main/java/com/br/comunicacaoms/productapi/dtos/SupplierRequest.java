package com.br.comunicacaoms.productapi.dtos;

import jakarta.validation.constraints.NotBlank;

public record SupplierRequest(@NotBlank String name) {
}
