package com.br.comunicacaoms.productapi.config.interceptor;

import com.br.comunicacaoms.productapi.config.RequestUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private final String AUTHORIZATION = "Authorization";
    private final String TRANSACTION_ID = "transactionid";

    @Override
    public void apply(RequestTemplate template) {
        var currentRequest = RequestUtils.getCurrentRequest();
        template
                .header(AUTHORIZATION,  currentRequest.getHeader(AUTHORIZATION))
                .header(TRANSACTION_ID, currentRequest.getHeader(TRANSACTION_ID));
    }
}
