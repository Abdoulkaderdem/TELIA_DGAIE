// package com.telia.Lease_management.utils;

// import org.apache.activemq.ActiveMQConnectionFactory;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.jms.annotation.EnableJms;
// import org.springframework.jms.connection.CachingConnectionFactory;
// import org.springframework.jms.core.JmsTemplate;
// import org.springframework.jms.support.converter.MessageConverter;
// import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
// import org.springframework.jms.support.converter.MessageType;;


// @Configuration
// @EnableJms
// public class JmsConfig {

//     @Bean
//     public ActiveMQConnectionFactory activeMQConnectionFactory() {
//         ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://10.5.27.220:61616");
//         factory.setUserName("admin");
//         factory.setPassword("admin");
//         return factory;
//     }

//     @Bean
//     public CachingConnectionFactory cachingConnectionFactory(ActiveMQConnectionFactory activeMQConnectionFactory) {
//         return new CachingConnectionFactory(activeMQConnectionFactory);
//     }

//     @Bean
//     public JmsTemplate jmsTemplate(CachingConnectionFactory cachingConnectionFactory) {
//         JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
//         jmsTemplate.setMessageConverter(messageConverter());
//         return jmsTemplate;
//     }

//     @Bean
//     public MessageConverter messageConverter() {
//         MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//         converter.setTargetType(MessageType.TEXT);
//         converter.setTypeIdPropertyName("_type");
//         return converter;
//     }

// }
