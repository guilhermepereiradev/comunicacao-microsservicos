package com.br.comunicacaoms.productapi.config;

import com.br.comunicacaoms.productapi.config.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class RequestUtils {

    private RequestUtils() {}

    public static HttpServletRequest getCurrentRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getRequest();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ValidationException("The current request could not be processed.");
        }
    }
}
