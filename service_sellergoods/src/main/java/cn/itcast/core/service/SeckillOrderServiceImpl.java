package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillOrderDao;

import cn.itcast.core.pojo.seckill.SeckillOrder;

import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderDao seckillOrderDao;


    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderDao.selectByExample(null);
    }

    @Override
    public List<SeckillOrder> search(SeckillOrder seckillOrder) {

        String sellerId = seckillOrder.getSellerId();
        List<SeckillOrder> seckillOrders = seckillOrderDao.selectListSeckillOrder(sellerId);

        return seckillOrders;
    }


}
