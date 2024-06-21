package com.zhaomsdemo.research.testcontainer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private PulsarTemplate<String> pulsarTemplate;

    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        pulsarTemplate.send("test", message);
        return message;
    }
}
