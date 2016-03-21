package ru.taximaxim.demo;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.taximaxim.demo.amq.SyncRequestor;
import ru.taximaxim.demo.context.SessionContext;

@RestController
public class IndexController {
    
    private final static String GETVERSION = "GETVERSION";
    private final static String REGISTER = "REGISTER";
    private final static String UNREGISTER = "UNREGISTER";

    @Autowired
    private SyncRequestor requestor;
    
    @Autowired
    private ConnectionFactory connectionFactory;
    
    @RequestMapping("/")
    public String getIndex() {
        //ap map = new HashMap();
        //map.put("Name", "MYNAME");
        //messageSender.send(map);
        Map<String, String> messParam = new HashMap<>();
        messParam.put("ACTION", "GETVERSION");
        
        MapMessage message = (MapMessage)requestor.syncRequest(messParam);
        
        try {
            System.err.println(message.getString("RESULT"));
            System.err.println(message.getString("VERSION"));
        } catch (JMSException e) {
            System.err.println(e);
        }
        
        messParam.clear();
        
        messParam.put("ACTION", "REGISTER");
        messParam.put("LOGIN", "petrov_im");
        messParam.put("PASSWORD", "peim125");
        
        message = (MapMessage)requestor.syncRequest(messParam);
        
        String queueName = null;
        try {
            queueName = message.getString("INFO-QUEUE");
            
            System.err.println(message.getString("RESULT"));
            System.err.println(message.getString("CONTEXT"));
            System.err.println(message.getString("INFO-QUEUE"));
            
            System.err.println(message.getString("MESSAGE"));
        } catch (JMSException e) {
            System.err.println(e);
        }
        
        if (queueName != null) {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.setDestinationName(queueName);
            container.setMessageListener(new MessageReceiver());
            container.start();
        }
        
        
        return "index";
    }
}