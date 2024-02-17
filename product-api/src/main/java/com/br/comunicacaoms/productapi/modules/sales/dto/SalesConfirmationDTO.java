package com.br.comunicacaoms.productapi.modules.sales.dto;

import com.br.comunicacaoms.productapi.modules.sales.enums.SalesStatus;

public record SalesConfirmationDTO(String salesId, SalesStatus status, String transactionid) {
}
