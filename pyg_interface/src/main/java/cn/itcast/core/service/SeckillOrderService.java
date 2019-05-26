package cn.itcast.core.service;


import cn.itcast.core.pojo.seckill.SeckillOrder;


import java.util.List;

public interface SeckillOrderService {
    List<SeckillOrder> findAll();

    List<SeckillOrder> search(SeckillOrder seckillOrder);
}
