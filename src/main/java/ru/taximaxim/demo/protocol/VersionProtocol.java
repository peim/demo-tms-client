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
public class VersionProtocol {

    private static final Logger LOG = Logger.getLogger(VersionProtocol.class);

    private static final String ACTION = "ACTION";
    private static final String GETVERSION = "GETVERSION";

    private Map<String, String> messageParam;
    
    @Autowired
    private SyncRequestor requestor;

    public VersionProtocol() {}

    public String getVersion() {
        try {
            MapMessage response = (MapMessage)requestor.syncRequest(getMessageParam());

            String result = response.getString("RESULT");
            String version = response.getString("VERSION");

            if (result != null && result.equals("SUCCESS")) {
                return version;
            }
        } catch (JMSException e) {
            LOG.error("Ошибка выполнения GETVERSION запроса: " + e);
        }
        return null;
    }

    private Map<String, String> getMessageParam() {
        if (messageParam == null) {
            messageParam = new HashMap<>();
            messageParam.put(ACTION, GETVERSION);
        }
        return messageParam;
    }
}
