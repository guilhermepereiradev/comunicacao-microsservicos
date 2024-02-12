package com.br.comunicacaoms.productapi.jwt;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
