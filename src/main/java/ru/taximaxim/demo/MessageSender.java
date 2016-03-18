package ru.taximaxim.demo;

import javax.jms.MapMessage;

import org.springframework.jms.core.JmsTemplate;

public class MessageSender {

    private final JmsTemplate jmsTemplate;

    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(MapMessage map) {
        jmsTemplate.convertAndSend(map);
    }

}