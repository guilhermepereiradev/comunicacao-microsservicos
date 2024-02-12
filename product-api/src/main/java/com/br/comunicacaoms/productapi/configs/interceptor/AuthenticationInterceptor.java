package com.br.comunicacaoms.productapi.configs.interceptor;

import com.br.comunicacaoms.productapi.modules.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isOption(request)) {
            return true;
        }

        var authorization = request.getHeader(AUTHORIZATION);
        jwtService.validateAuthorization(authorization);

        return true;
    }

    private boolean isOption(HttpServletRequest request) {
        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }
}
