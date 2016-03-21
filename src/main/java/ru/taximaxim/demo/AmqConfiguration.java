package ru.taximaxim.demo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
public class AmqConfiguration {

    @Value("${spring.activemq.broker-url}")
    private String brokerURL;

    @Bean
    public ActiveMQConnectionFactory amqConnectionFactory() {
        ActiveMQConnectionFactory amqConnectionFactory = 
                new ActiveMQConnectionFactory(brokerURL);
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
