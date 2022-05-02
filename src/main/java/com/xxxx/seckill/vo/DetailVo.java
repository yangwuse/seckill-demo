package com.xxxx.seckill.vo;

import com.xxxx.seckill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品详情对象
 * @author yangWu
 * @date 2022/4/24 9:45 AM
 * @version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {
    private User user;
    private GoodsVo goodsVo;
    private int secKillStatus;
    private int remainSeconds;
}
