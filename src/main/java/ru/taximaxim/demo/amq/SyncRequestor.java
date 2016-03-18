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

public class SyncRequestor {
 
    private final JmsTemplate jmsTemplate;
 
    @Autowired
    public SyncRequestor(JmsTemplate jmsTemplate ) {
        this.jmsTemplate = jmsTemplate;
     }
 
    public Message syncRequest(Map<String, String> messageParam) {
        // Must pass true as the second param to start the connection
       // return jmsTemplate.execute(new SyncRequestResponse(messageParam), true);
        
        return jmsTemplate.execute(new SessionCallback<MapMessage>() {

            private static final int TIMEOUT = 5000;
            
            private static final String PRIMARY_QUEUE_NAME = "TMS";
            
            public MapMessage doInJms(Session session) throws JMSException {
                MessageConsumer consumer = null;
                MessageProducer producer = null;
                try {
                    Destination requestQueue = session.createQueue(PRIMARY_QUEUE_NAME);
                    Destination replyQueue = session.createTemporaryQueue();
                    
                    // Create the consumer first!
                    consumer = session.createConsumer(replyQueue);
                    
                    MapMessage message = session.createMapMessage();
                    for (Map.Entry<String, String> param : messageParam.entrySet()) {
                        message.setString(param.getKey(), param.getValue());
                    }
                    
                    String correlationId = UUID.randomUUID().toString();
                    message.setJMSCorrelationID(correlationId);
                    message.setJMSReplyTo(replyQueue);
                    
                    // Send the request second!
                    producer = session.createProducer(requestQueue);
                    producer.send(requestQueue, message);
                    
                    // Block on receiving the response with a timeout
                    return (MapMessage)consumer.receive(TIMEOUT);
                }
                finally {
                    // Don't forget to close your resources
                    JmsUtils.closeMessageConsumer(consumer);
                    JmsUtils.closeMessageProducer(producer);
                }
            }
        });
    }
}