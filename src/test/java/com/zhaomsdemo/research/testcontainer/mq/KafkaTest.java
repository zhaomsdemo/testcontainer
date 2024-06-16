package com.zhaomsdemo.research.testcontainer.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
public class KafkaTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private CountDownLatch latch = new CountDownLatch(1);

    static KafkaContainer kafkaContainer = new KafkaContainer("apache/kafka:latest").withExposedPorts(9092);

    @DynamicPropertySource
    public static void registryProperties(DynamicPropertyRegistry registry) {

        kafkaContainer.start();
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Test
    public void testProductAndConsumeMessage() {

        kafkaTemplate.send("my-topic", "my-key", "my-value");
    }

    @KafkaListener(topics = {"my-topic"}, groupId = "my-group")
    public void consumeMessage(ConsumerRecord<?, ?> consumerRecord) {
        String message = consumerRecord.value().toString();
        assertNotNull(message);
        assertEquals("my-value", message);
        latch.countDown();
    }
}
