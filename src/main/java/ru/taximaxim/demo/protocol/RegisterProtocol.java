package ru.taximaxim.demo.protocol;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.taximaxim.demo.amq.SyncRequestor;

@Service
public class RegisterProtocol {
    
    private static final Logger LOG = Logger.getLogger(RegisterProtocol.class);
    
    private static final String ACTION = "ACTION";
    private static final String REGISTER = "REGISTER";
    private static final String LOGIN = "LOGIN";
    private static final String PASSWORD = "PASSWORD";

    private Map<String, String> messageParam;

    @Autowired
    private SyncRequestor requestor;
    
    public RegisterProtocol() {}
    
    public RegisterResponse register(String login, String password) {
        try {
            MapMessage response = (MapMessage)requestor.syncRequest(
                    getMessageParam(login, password));

            String result = response.getString("RESULT");
            String errMessage = response.getString("MESSAGE");

            if (result != null && result.equals("SUCCESS")) {
                return new RegisterResponse(
                        response.getString("CONTEXT"),
                        response.getString("INFO-QUEUE"),
                        response.getString("CANSPYOPERATOR"));
            } else {
                LOG.error("Ошибка выполнения REGISTER запроса: " + errMessage);
            }
        } catch (JMSException e) {
            LOG.error("Ошибка выполнения REGISTER запроса: " + e);
        }
        return null;
    }
    
    private Map<String, String> getMessageParam(String login, String password) {
        if (messageParam == null) {
            messageParam = new HashMap<>();
            messageParam.put(ACTION, REGISTER);
        }
        messageParam.put(LOGIN, login);
        messageParam.put(PASSWORD, password);
        return messageParam;
    }

}
