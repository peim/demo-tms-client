package ru.taximaxim.demo.controller;

import javax.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.taximaxim.demo.amq.MessageReceiver;
import ru.taximaxim.demo.context.SessionContext;
import ru.taximaxim.demo.protocol.RegisterProtocol;
import ru.taximaxim.demo.protocol.RegisterResponse;
import ru.taximaxim.demo.protocol.VersionProtocol;
import ru.taximaxim.demo.utils.AmqUtils;

@RestController
public class IndexController {

    @Autowired
    private VersionProtocol versionProtocol;

    @Autowired
    private RegisterProtocol registerProtocol;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private SessionContext sessionContext;

   /* @RequestMapping("/")
    public String getIndex() {

        if (AmqUtils.checkProtocolVersion(versionProtocol.getVersion())) {
            RegisterResponse response = registerProtocol.register("petrov_im", "peim125");
            if (response.getQueue() != null) {
                SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
                container.setConnectionFactory(connectionFactory);
                container.setDestinationName(response.getQueue());
                container.setMessageListener(new MessageReceiver());
                container.start();
            }
            sessionContext.setUser("petrov_im");
            sessionContext.setContext(response.getContext());
        }
        return "index";
    }*/
}