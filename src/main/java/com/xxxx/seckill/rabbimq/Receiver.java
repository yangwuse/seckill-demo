package com.xxxx.seckill.rabbimq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.seckill.entity.Order;
import com.xxxx.seckill.entity.SeckillMsg;
import com.xxxx.seckill.entity.SeckillOrder;
import com.xxxx.seckill.entity.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 *
 * @author yangWu
 * @date 2022/4/26 11:45 AM
 * @version 1.0
 */

@Service
@Slf4j
public class Receiver {

    // @RabbitListener(queues = "queue")
    // public void receive(Object msg) {
    //     log.info("接受消息: " + msg);
    // }
    //
    // @RabbitListener(queues = "queue_fanout01")
    // public void receive01(Object msg) {
    //     log.info("QUEUE01 接受消息: " + msg);
    // }
    //
    // @RabbitListener(queues = "queue_fanout02")
    // public void receive02(Object msg) {
    //     log.info("QUEUE02 接受消息: " + msg);
    // }
    //
    // @RabbitListener(queues = "queue_direct01")
    // public void receive03(Object msg) {
    //     log.info("QUEUE01 接受消息: " + msg);
    // }
    //
    // @RabbitListener(queues = "queue_direct02")
    // public void receive04(Object msg) {
    //     log.info("QUEUE02 接受消息: " + msg);
    // }
    //
    // @RabbitListener(queues = "queue_topic01")
    // public void receive05(Object msg) {
    //     log.info("QUEUE01 接受消息: " + msg);
    // }
    //
    // @RabbitListener(queues = "queue_topic02")
    // public void receive06(Object msg) {
    //     log.info("QUEUE02 接受消息: " + msg);
    // }

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;

    // 下单操作
    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg) {
        log.info("接受消息: " + msg);
        SeckillMsg seckillMsg = JsonUtil.jsonStr2Object(msg, SeckillMsg.class);
        Long goodsId = seckillMsg.getGoodsId();
        User user = seckillMsg.getUser();

        // 判断库存是否足够
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1)
            return ;

        // 判断是否重复抢购
        ValueOperations valueOperations = redisTemplate.opsForValue();
        SeckillOrder order = ((SeckillOrder) valueOperations.get("order:" + user.getId() + ":" + goodsId));
        if (order != null)
            return ;

        // 下单操作
        orderService.seckill(user, goodsVo);
    }

}
