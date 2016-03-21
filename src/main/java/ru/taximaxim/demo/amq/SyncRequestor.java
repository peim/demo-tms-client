package ru.taximaxim.demo.amq;

import java.util.Map;
import java.util.UUID;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.SessionCallback;
import org.springframework.jms.support.JmsUtils;
import org.springframework.stereotype.Service;

@Service
public class SyncRequestor {

    private static final int TIMEOUT = 5000;

    private static final String PRIMARY_QUEUE_NAME = "TMS";

    @Autowired
    private JmsTemplate jmsTemplate;

    public SyncRequestor() {}

    public Message syncRequest(Map<String, String> messageParam) {
        return jmsTemplate.execute(new SessionCallback<MapMessage>() {

            @Override
            public MapMessage doInJms(Session session) throws JMSException {
                MessageConsumer consumer = null;
                MessageProducer producer = null;
                try {
                    Destination requestQueue = session.createQueue(PRIMARY_QUEUE_NAME);
                    Destination replyQueue = session.createTemporaryQueue();

                    consumer = session.createConsumer(replyQueue);

                    MapMessage message = session.createMapMessage();
                    for (Map.Entry<String, String> param : messageParam.entrySet()) {
                        message.setString(param.getKey(), param.getValue());
                    }

                    String correlationId = UUID.randomUUID().toString();
                    message.setJMSCorrelationID(correlationId);
                    message.setJMSReplyTo(replyQueue);

                    producer = session.createProducer(requestQueue);
                    producer.send(requestQueue, message);

                    return (MapMessage)consumer.receive(TIMEOUT);
                }
                finally {
                    JmsUtils.closeMessageConsumer(consumer);
                    JmsUtils.closeMessageProducer(producer);
                }
            }
        }, true);
    }
}