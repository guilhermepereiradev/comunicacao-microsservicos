package com.br.comunicacaoms.productapi.modules.product.dto;

import java.util.List;

public record ProductCheckStockRequest(List<ProductQuantityDTO> products) {
}
