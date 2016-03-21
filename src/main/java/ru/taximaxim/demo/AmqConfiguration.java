package ru.taximaxim.demo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

@Configuration
public class AmqConfiguration {

    @Value("${spring.activemq.user}")
    private String userName;
    
    @Value("${spring.activemq.password}")
    private String password;
    
    @Value("${spring.activemq.broker-url}")
    private String brokerURL;

    @Bean
    public ActiveMQConnectionFactory amqConnectionFactory() {
        ActiveMQConnectionFactory amqConnectionFactory = 
                new ActiveMQConnectionFactory("tcp://10.145.251.16:61701");
               // new ActiveMQConnectionFactory(userName, password, brokerURL);
        return amqConnectionFactory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(amqConnectionFactory());
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        return jmsTemplate;
    }
}
