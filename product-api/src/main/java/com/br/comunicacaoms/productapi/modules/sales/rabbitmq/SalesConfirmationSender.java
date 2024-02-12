package com.br.comunicacaoms.productapi.modules.sales.rabbitmq;

import com.br.comunicacaoms.productapi.modules.sales.dto.SalesConfirmationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalesConfirmationSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationKey;

    public void sendSalesConfirmationMessage(SalesConfirmationDTO message) {
        try {
            log.info("Sending message: {}", new ObjectMapper().writeValueAsString(message));
            rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationKey, message);
            log.info("Message was send successfully.");
        } catch (Exception ex) {
            log.error("Error while trying send sales confirmation message: ", ex);
        }
    }
}
