package com.br.comunicacaoms.productapi.config.interceptor;

import com.br.comunicacaoms.productapi.config.exceptions.ValidationException;
import com.br.comunicacaoms.productapi.modules.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final String AUTHORIZATION = "Authorization";
    private final String TRANSACTION_ID = "transactionid";
    private final String SERVICE_ID = "serviceid";

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isOption(request)) {
            return true;
        }

        if (request.getHeader(TRANSACTION_ID) == null || request.getHeader(TRANSACTION_ID).isBlank()) {
            throw new ValidationException("The transactionid header is required.");
        }

        var authorization = request.getHeader(AUTHORIZATION);
        jwtService.validateAuthorization(authorization);

        request.setAttribute(SERVICE_ID, UUID.randomUUID().toString());
        return true;
    }

    private boolean isOption(HttpServletRequest request) {
        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }
}
