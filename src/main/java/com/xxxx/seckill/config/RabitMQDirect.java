// package com.xxxx.seckill.config;
//
// import org.springframework.amqp.core.*;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// /**
//  *
//  * @author yangWu
//  * @date 2022/4/26 3:31 PM
//  * @version 1.0
//  */
//
// @Configuration
// public class RabitMQDirect {
//
//     private static final String QUEUE01 = "queue_direct01";
//     private static final String QUEUE02 = "queue_direct02";
//     private static final String EXCHANGE = "directExchange";
//     private static final String ROUTINGKEY01 = "queue.red";
//     private static final String ROUTINGKEY02 = "queue.green";
//
//     @Bean
//     public Queue mq01() {
//         return new Queue(QUEUE01);
//     }
//
//     @Bean
//     public Queue mq02() {
//         return new Queue(QUEUE02);
//     }
//
//     @Bean
//     public DirectExchange directExchange() {
//         return new DirectExchange(EXCHANGE);
//     }
//
//     @Bean
//     public Binding bind01() {
//         return BindingBuilder.bind(mq01()).to(directExchange()).with(ROUTINGKEY01);
//     }
//
//     @Bean Binding bind02() {
//         return BindingBuilder.bind(mq02()).to(directExchange()).with(ROUTINGKEY02);
//     }
//
// }
