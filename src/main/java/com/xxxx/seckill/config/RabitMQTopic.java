package com.xxxx.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author yangWu
 * @date 2022/4/26 4:59 PM
 * @version 1.0
 */
@Configuration
public class RabitMQTopic {

    // private static final String QUEUE01 = "queue_topic01";
    // private static final String QUEUE02 = "queue_topic02";
    // private static final String EXCHANGE = "topicExchange";
    // private static final String ROUTINTKEY01 = "#.queue.#";
    // private static final String ROUTINGKEY02 = "*.queue.#";
    //
    // @Bean
    // public Queue queue_topic01() {
    //     return new Queue(QUEUE01);
    // }
    //
    // @Bean
    // public Queue queue_topic02() {
    //     return new Queue(QUEUE02);
    // }
    //
    // @Bean
    // public TopicExchange topicExchange() {
    //     return new TopicExchange(EXCHANGE);
    // }
    //
    // @Bean
    // public Binding bind_queue_topic01() {
    //     return BindingBuilder.bind(queue_topic01()).to(topicExchange()).with(ROUTINTKEY01);
    // }
    //
    // @Bean
    // public Binding bind_queue_topic02() {
    //     return BindingBuilder.bind(queue_topic02()).to(topicExchange()).with(ROUTINGKEY02);
    // }



    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }

}
