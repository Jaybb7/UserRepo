package com.eCommerce.UserModule.Service;

import com.eCommerce.UserModule.Model.OrderRequest;
import com.eCommerce.UserModule.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final KafkaTemplate<String, OrderRequest> kafkaTemplate;

    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Value("${order.topic.name}")
    private String orderTopic;

    public void createOrder(OrderRequest orderRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        orderRequest.setUserId(userRepository.findByUsername(auth.getName()).getId());
        logger.info("Order request send to kafka topic");
        kafkaTemplate.send(orderTopic, orderRequest);
    }

}
