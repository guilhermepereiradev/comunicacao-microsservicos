package com.br.comunicacaoms.productapi.rabbitmq;

import com.br.comunicacaoms.productapi.dtos.ProductStockDTO;
import com.br.comunicacaoms.productapi.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductStockListener {

    private final ProductService productService;

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void receiverProductStockMessage(ProductStockDTO product) throws JsonProcessingException {

        log.info("Recebendo mensagem: {}", new ObjectMapper().writeValueAsString(product));
        productService.updateProductStock(product);
    }
}
