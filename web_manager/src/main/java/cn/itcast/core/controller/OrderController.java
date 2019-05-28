package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import cn.itcast.core.util.ExportExcelUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author: 12778
 * @Date: 2019/5/27 17:19
 * @Description:
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/search")
    public PageResult search(@RequestBody Order goods, Integer page, Integer rows) {

        //3. 进行分页查询
        PageResult pageResult = orderService.search(goods, page, rows);
        return pageResult;
    }

    @RequestMapping("/Epgood")
    public void  ExportGood(long[] ids,HttpServletResponse response){
        if (ids !=null&& ids.length >0 ){
            //要导出的数据
            List<Order> goodsList = orderService.ExportGood(ids);
            //生成的excel 的名字
            String name  ="订单数据"+ UUID.randomUUID().toString().replace("-","");
            // 获取需要转出的excle表头的map字段
            LinkedHashMap<String, String> fieldMap =new LinkedHashMap<String, String>() ;
            fieldMap.put("orderId", "订单ID");
            fieldMap.put("payment", "实付金额");
            fieldMap.put("paymentType", "支付类型");
            fieldMap.put("postFee", "邮费");
            fieldMap.put("status", "状态");
            fieldMap.put("createTime", "订单创建时间");
            fieldMap.put("updateTime", "订单更新时间");
            fieldMap.put("paymentTime", "付款时间");
            fieldMap.put("consignTime", "发货时间");
            fieldMap.put("endTime", "交易完成时间");
            fieldMap.put("closeTime", "交易关闭时间");
            fieldMap.put("shippingName", "物流名称");
            fieldMap.put("shippingCode", "物流单号");
            fieldMap.put("userId", "用户id");
            fieldMap.put("buyerMessage", "买家留言");
            fieldMap.put("buyerNick", "买家昵称");
            fieldMap.put("buyerRate", "买家是否已经评价");
            fieldMap.put("receiverAreaName", "收货人地区名称");
            fieldMap.put("receiverMobile", "收货人手机");
            fieldMap.put("receiverZipCode", "收货人邮编");
            fieldMap.put("receiver", "收货人");
            fieldMap.put("expire", "过期时间");
            fieldMap.put("invoiceType", "发票类型");
            fieldMap.put("sourceType", "订单来源");
            fieldMap.put("sellerId", "商家ID");
            ExportExcelUtils.export(name, goodsList, fieldMap, response);
        }else {
            try {
                response.getWriter().write("ID为空或者错误");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
