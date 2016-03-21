package ru.taximaxim.demo;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class MessageReceiver implements MessageListener {

    public void onMessage(Message message) {
        if (message instanceof MapMessage) {
            final MapMessage mapMessage = (MapMessage) message;
            
            System.err.println("Пришло: " + mapMessage);
        }
    }

}