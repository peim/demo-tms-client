package ru.taximaxim.demo;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ru.taximaxim.demo.amq.SyncRequestor;
import ru.taximaxim.demo.context.SessionContext;

@Component
public class KeepAliveTask {

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private SyncRequestor requestor;

    public KeepAliveTask() {}
    
    @Scheduled(fixedRate = 60000)
    public void sendKeepAlive() {
        System.err.println("KEEPALIVE - " + sessionContext.getContext());
        MapMessage response = null;
        if (sessionContext.getContext() != null) {
            Map<String, String> messageParam = new HashMap<String, String>();
            messageParam.put("ACTION", "KEEPALIVE");
            messageParam.put("CONTEXT", sessionContext.getContext());
            response = (MapMessage)requestor.syncRequest(messageParam);
            
            try {
                if (response.getString("MESSAGE") != null) {
                    System.err.println("Ошибка: " + response.getString("MESSAGE"));
                }
            } catch (JMSException e) {
                System.err.println(e);
            }
        }
    }
}
