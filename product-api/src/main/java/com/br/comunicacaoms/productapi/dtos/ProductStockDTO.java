package com.br.comunicacaoms.productapi.dtos;

import java.util.List;

public record ProductStockDTO(String salesId, List<ProductQuantityDTO> products) {
}
