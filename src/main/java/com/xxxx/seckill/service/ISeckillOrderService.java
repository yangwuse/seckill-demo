package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.entity.SeckillOrder;
import com.xxxx.seckill.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yangwu
 * @since 2022-04-09
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    Long getResult(User user, Long goodsId);
}
