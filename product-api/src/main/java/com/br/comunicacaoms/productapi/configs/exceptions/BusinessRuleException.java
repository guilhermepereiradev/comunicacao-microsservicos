package com.br.comunicacaoms.productapi.configs.exceptions;

public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
