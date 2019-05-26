package cn.itcast.core.controller;


import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillOrderService")
public class SeckillOrderController {

    @Reference
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/findAll")
    public List<SeckillOrder>findAll(){
        return seckillOrderService.findAll();
    }


    @RequestMapping("/search")
    public List<SeckillOrder> search(@RequestBody(required = false)SeckillOrder seckillOrder){
        return seckillOrderService.search(seckillOrder);
    }
}
