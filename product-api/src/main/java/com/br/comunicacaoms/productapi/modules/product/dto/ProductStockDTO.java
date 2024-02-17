package com.br.comunicacaoms.productapi.modules.product.dto;

import java.util.List;

public record ProductStockDTO(String salesId, List<ProductQuantityDTO> products, String transactionid) {
}
