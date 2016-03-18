package ru.taximaxim.demo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

import ru.taximaxim.demo.amq.SyncRequestor;

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

   /* @Bean
    public ActiveMQQueue amqQueue() {
        return new ActiveMQQueue("queue1");
    }*/
    
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        //jmsTemplate.setDefaultDestination(amqQueue());
        return jmsTemplate;
    }
    
    /*@Bean
    public MessageSender messageSender() {
        return new MessageSender(jmsTemplate());
    }*/
    
    @Bean
    public SyncRequestor requestor() {
        return new SyncRequestor(jmsTemplate());
    }
    
    /*@Bean
    public MessageReceiver messageReceiver() {
        return new MessageReceiver();
    }*/
    
   /* @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer listener = new SimpleMessageListenerContainer();
        listener.setConnectionFactory(connectionFactory());
        //listener.setDestination(amqQueue());
        listener.setMessageListener(messageReceiver());
        return listener;
    }*/
}
