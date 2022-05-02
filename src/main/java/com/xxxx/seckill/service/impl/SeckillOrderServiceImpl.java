package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.entity.SeckillOrder;
import com.xxxx.seckill.entity.User;
import com.xxxx.seckill.mapper.SeckillOrderMapper;
import com.xxxx.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangwu
 * @since 2022-04-09
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    private SeckillOrderMapper mapper;
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder order = mapper.selectOne(new QueryWrapper<SeckillOrder>()
            .eq("user_id", user.getId())
            .eq("goods_id", goodsId)
        );
        return order.getOrderId();
    }
}
