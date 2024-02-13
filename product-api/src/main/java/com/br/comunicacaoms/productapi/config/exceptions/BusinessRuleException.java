package com.br.comunicacaoms.productapi.config.exceptions;

public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
