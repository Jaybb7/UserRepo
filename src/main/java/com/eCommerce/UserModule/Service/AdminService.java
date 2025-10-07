package com.eCommerce.UserModule.Service;

import com.eCommerce.UserModule.Entity.User;
import com.eCommerce.UserModule.Enums.OrderStatus;
import com.eCommerce.UserModule.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final Logger logger = Logger.getLogger(AdminService.class.getName());

    private final WebClient webClient;

    public void promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    public boolean modifyOrder(Long orderId, OrderStatus orderStatus, String comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correlationId = UUID.randomUUID().toString();
        logger.info("Admin requesting changes with Correlation ID: " + correlationId + " by user: " + auth.getName());

        try {
            Boolean response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("localhost")
                            .port(8082)
                            .path("/orders/modifyOrder")
                            .queryParam("orderId", orderId)
                            .queryParam("orderStatus", orderStatus != null ? orderStatus.name() : null)
                            .queryParam("comment", comment)
                            .build())
                    .header("CorrelationId", correlationId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            logger.info("Order modification response: " + response);
            return Boolean.TRUE.equals(response);

        } catch (Exception e) {
            logger.severe("Error while modifying order with Correlation ID " + correlationId + ": " + e);
            return false;
        }
    }
}
