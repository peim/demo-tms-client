package ru.taximaxim.demo.amq;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.taximaxim.demo.context.SessionContextHolder;

@Service
public class KeepAliveTask {

    @Autowired
    private SyncRequestor requestor;

    @Autowired
    private SessionContextHolder sessionContextHolder;
    
    public KeepAliveTask() {}

    @Scheduled(fixedRate = 60000)
    public void sendKeepAlive() {
        for (String context : sessionContextHolder.getContexts()) {
            System.err.println("KEEPALIVE - " + context);
            
            MapMessage response = null;
            if (context != null) {
                Map<String, String> messageParam = new HashMap<String, String>();
                messageParam.put("ACTION", "KEEPALIVE");
                messageParam.put("CONTEXT", context);
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
}
