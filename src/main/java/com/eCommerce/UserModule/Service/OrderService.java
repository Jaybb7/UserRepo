package com.eCommerce.UserModule.Service;

import com.eCommerce.UserModule.Model.OrderRequest;
import com.eCommerce.UserModule.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final KafkaTemplate<String, OrderRequest> kafkaTemplate;

    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Value("${order.topic.name}")
    private String orderTopic;

    public void createOrder(OrderRequest orderRequest) {
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);
        logger.info("Entered createOrder method, correlationId={}", correlationId);
        try {
            logger.debug("Starting authentication retrieval for order creation, correlationId={}", correlationId);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                logger.error("Authentication object is null, correlationId={}", correlationId);
                throw new IllegalStateException("No authentication found in security context");
            }
            logger.info("Authenticated user: {}, authorities: {}, correlationId={}", auth.getName(), auth.getAuthorities(), correlationId);
            Long userId = userRepository.findByUsername(auth.getName()).getId();
            orderRequest.setUserId(userId);
            logger.debug("User ID set in orderRequest: {}, correlationId={}", userId, correlationId);
            logger.info("Preparing to send order to Kafka topic '{}', correlationId={}", orderTopic, correlationId);
            ProducerRecord<String, OrderRequest> record = new ProducerRecord<>(orderTopic, orderRequest);
            record.headers().add("correlationId", correlationId.getBytes(StandardCharsets.UTF_8));
            kafkaTemplate.send(record);
            logger.info("Order sent to Kafka topic '{}', correlationId={}", orderTopic, correlationId);
        } catch (Exception e) {
            logger.error("Error occurred while creating order, correlationId={}", correlationId, e);
            throw e;
        } finally {
            MDC.clear();
            logger.debug("MDC cleared, correlationId={}", correlationId);
            logger.info("Exiting createOrder method, correlationId={}", correlationId);
        }
    }

}
