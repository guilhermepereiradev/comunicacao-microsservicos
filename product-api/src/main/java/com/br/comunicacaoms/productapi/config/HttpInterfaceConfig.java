package com.br.comunicacaoms.productapi.config;

import com.br.comunicacaoms.productapi.modules.sales.clients.SalesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Value("${app-config.services.sales}")
    private String baseUrl;

    @Bean
    public SalesClient salesClient() {
        return HttpServiceProxyFactory
                .builderFor(
                        WebClientAdapter
                                .create(WebClient.create(baseUrl))
                ).build()
                .createClient(SalesClient.class);
    }
}
