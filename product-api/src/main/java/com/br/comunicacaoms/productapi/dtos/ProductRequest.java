package com.br.comunicacaoms.productapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(@NotBlank
                             String name,
                             @NotNull
                             @Positive
                             Integer categoryId,

                             @NotNull
                             @Positive
                             Integer supplierId,

                             @NotNull
                             @Positive
                             Integer quantityAvailable) {
}
