package com.br.comunicacaoms.productapi.config.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
