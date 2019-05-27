package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: tjc
 * @Date: 2019/5/25 11:44
 * @Description:
 */
@Controller
@RequestMapping("/Order")
public class OrderController {

    @Reference
    private OrderService orderService;

    /**
     * 查询当前用户的订单
     * @return
     *
     */
    @RequestMapping("/findAll")
    @ResponseBody
    public List<Order> findAll(){
        //获取当前登录的用户
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name == null && "".equals(name)){
            //没有登录
            return null;
        }
        List<Order> orderList= orderService.findAllByName(name);
        return orderList;
    }
}
