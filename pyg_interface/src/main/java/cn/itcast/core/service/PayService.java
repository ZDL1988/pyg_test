package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;

import java.util.Map;

public interface PayService {

    /**
     * 调用微信统一下单接口生成支付链接
     *
     * @param tradeNo  支付单号
     * @param totalFee 总金额
     * @return
     */
    public Map createNative(String tradeNo, String totalFee);

    /**
     * 根据支付单号查询支付状态
     *
     * @param tradeNo 支付单号
     * @return
     */
    public Map queryPayStatus(String tradeNo);

    /**
     * 查询未支付订单
     *
     * @param userName
     * @return
     */
    PageResult findOrderListByPage(String userName,Integer page, Integer rows);
}
