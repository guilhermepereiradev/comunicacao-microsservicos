package com.br.comunicacaoms.productapi.modules.jwt.service;

import com.br.comunicacaoms.productapi.config.exceptions.AuthenticationException;
import com.br.comunicacaoms.productapi.modules.jwt.dto.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Slf4j
public class JwtService {

    private static final Integer TOKEN_INDEX = 1;

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    public void validateAuthorization(String token) {
        var accessToken = extractToken(token);
        try {
            var claims = Jwts
                    .parser()
                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            var user = JwtResponse.getUser(claims);

            if(isEmpty(user) || isEmpty(user.getId())) {
                throw new AuthenticationException("User is not valid.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AuthenticationException("Error while trying to process access Token.");
        }
    }

    private String extractToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new AuthenticationException("Access token was not informed.");
        }

        if(token.toLowerCase().contains(" ")) {
            return token.split(" ")[TOKEN_INDEX];
        }

        return token;
    }
}
