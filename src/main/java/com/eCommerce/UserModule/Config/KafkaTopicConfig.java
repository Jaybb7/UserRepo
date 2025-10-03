package com.eCommerce.UserModule.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${order.topic.name}")
    private String orderTopicName;

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name(orderTopicName)
                .partitions(3)
                .replicas(1)
                .build();
    }
}