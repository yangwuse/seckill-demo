package com.xxxx.seckill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author yangWu
 * @date 2022/4/27 11:06 AM
 * @version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMsg {

    private User user;
    private Long goodsId;
}
