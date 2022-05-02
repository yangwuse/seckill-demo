package com.xxxx.seckill.rabbimq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

/**
 * 消息发送者
 * @author yangWu
 * @date 2022/4/26 11:37 AM
 * @version 1.0
 */

@Service
@Slf4j
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // public void send(Object msg) {
    //     log.info("发送消息: " + msg);
    //     rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
    // }
    //
    // public void send01(Object msg) {
    //     log.info("发送 red 消息: " + msg);
    //     rabbitTemplate.convertAndSend("directExchange", "queue.red", msg);
    // }
    //
    // public void send02(Object msg) {
    //     log.info("发送 green 消息: " + msg);
    //     rabbitTemplate.convertAndSend("directExchange", "queue.green", msg);
    // }
    //
    // public void send03(Object msg) {
    //     log.info("发送消息(queue01接受): " + msg);
    //     rabbitTemplate.convertAndSend("topicExchange", "queue.red.msg", msg);
    // }
    //
    // public void send04(Object msg) {
    //     log.info("发送消息(queue01 queue02 接受): " + msg);
    //     rabbitTemplate.convertAndSend("topicExchange", "msg.queue.green", msg);
    // }


    // 发送秒杀信息
    public void sendSeckillMsg(String msg) {
        log.info("发送消息: " + msg);
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.message", msg);
    }
}
