package com.br.comunicacaoms.productapi.modules.supplier.dto;

import jakarta.validation.constraints.NotBlank;

public record SupplierRequest(@NotBlank String name) {
}
