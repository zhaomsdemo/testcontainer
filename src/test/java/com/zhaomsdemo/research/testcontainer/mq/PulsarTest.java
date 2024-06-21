package com.zhaomsdemo.research.testcontainer.mq;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.client.impl.PulsarClientImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.pulsar.annotation.EnablePulsar;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PulsarContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
@EnablePulsar
public class PulsarTest {

    @Autowired
    private PulsarTemplate<String> pulsarTemplate;

    @Autowired
    private PulsarClient pulsarClient;
    private Producer<String> pulsarProducer;

    static PulsarContainer container = new PulsarContainer("3.3.0").
            withExposedPorts(8080,6650);

    @DynamicPropertySource
    public static void registryProperties(DynamicPropertyRegistry registry) {
        container.start();
        registry.add("spring.pulsar.client.service-url", container::getPulsarBrokerUrl);
    }

    @Test
    public void testPublishMessage() throws Exception {
        String topic = "test-topic";
        String message = "This is a test message";
//        pulsarTemplate.send(topic, message);
        Producer<String> producer = pulsarClient.newProducer(Schema.STRING)
                .topic(topic)
                .create();
        producer.send(message);
        producer.close();

        Consumer<String> consumer = pulsarClient.newConsumer(Schema.STRING)
                .topic(topic)
                .subscriptionName("test-subscription")
                .subscribe();
        consumer.receiveAsync().thenCompose(stringMessage -> {
            assertNotNull(stringMessage);
            System.out.println("Message received : " + stringMessage);
            return null;
        });
    }

    @PulsarListener(topics = {"test-topic"}, subscriptionName = "test-sub")
    public void receive(String message) {
        assertNotNull(message);
        assertEquals("This is a test message",message);
    }
}
