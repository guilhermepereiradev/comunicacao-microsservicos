package com.br.comunicacaoms.productapi.modules.sales.clients;

import com.br.comunicacaoms.productapi.modules.sales.dto.SalesProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "salesCliente",
        contextId = "salesCliente",
        url = "${app-config.services.sales}"
)
public interface SalesClient {

    @GetMapping("/products/{productId}")
    Optional<SalesProductResponse> findSalesByProductId(@PathVariable Integer productId);
}
