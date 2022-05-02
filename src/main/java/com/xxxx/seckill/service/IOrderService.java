package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.entity.Order;
import com.xxxx.seckill.entity.User;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangwu
 * @since 2022-04-09
 */
public interface IOrderService extends IService<Order> {

    // 秒杀
    Order seckill(User user, GoodsVo goods);

    // 订单详情
    OrderDetailVo detail(Long orderId);

    // 获取秒杀地址
    String createPath(User user, Long goodsId);

    // 校验秒杀地址
    boolean checkPath(User user, Long goodsId, String path);

    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
