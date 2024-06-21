package com.zhaomsdemo.research.testcontainer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageListener {

    @PulsarListener(topics = "test")
    public void onMessage(String message) {
        log.info("Retrieve message {]", message);
    }
}
