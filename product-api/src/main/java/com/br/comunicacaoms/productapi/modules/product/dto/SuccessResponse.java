package com.br.comunicacaoms.productapi.modules.product.dto;

import org.springframework.http.HttpStatus;

public record SuccessResponse(Integer status, String message) {

    public SuccessResponse(String message) {
        this(HttpStatus.OK.value(), message);
    }
}
