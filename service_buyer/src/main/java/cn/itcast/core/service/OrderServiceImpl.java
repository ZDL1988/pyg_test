package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.entity.BuyerCart;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.util.Constants;
import cn.itcast.core.util.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private PayLogDao payLogDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ItemDao itemDao;
    @Override
    public void add(Order order, List<BuyerCart> cartList) {
        /**
         * 1. 遍历购物车集合
         * 每一个购物车对象, 都保存一条order订单表数据
         */
        List<String> orderIdList=new ArrayList();//订单ID列表
        double total_money=0;//总金额 （元）
        for(BuyerCart cart:cartList){
            //生成一个id
            long orderId = idWorker.nextId();
            System.out.println("sellerId:"+cart.getSellerId());
            Order tborder=new Order();//新创建订单对象
            tborder.setOrderId(orderId);//订单ID
            tborder.setUserId(order.getUserId());//用户名
            tborder.setPaymentType(order.getPaymentType());//支付类型
            tborder.setStatus("1");//状态：未付款
            tborder.setCreateTime(new Date());//订单创建日期
            tborder.setUpdateTime(new Date());//订单更新日期
            tborder.setReceiverAreaName(order.getReceiverAreaName());//地址
            tborder.setReceiverMobile(order.getReceiverMobile());//手机号
            tborder.setReceiver(order.getReceiver());//收货人
            tborder.setSourceType(order.getSourceType());//订单来源
            tborder.setSellerId(cart.getSellerId());//商家ID


            /**
             * 2. 遍历购物项集合
             * 每一个购物项对象, 都保存一条order_item订单详情表数据
             */
            double money=0;
            for(OrderItem orderItem :cart.getOrderItemList()){
                orderItem.setId(idWorker.nextId());
                orderItem.setOrderId( orderId  );//订单ID
                orderItem.setSellerId(cart.getSellerId());
                money+=orderItem.getTotalFee().doubleValue();//金额累加
                orderItemDao.insertSelective(orderItem);
            }

            tborder.setPayment(new BigDecimal(money));
            orderDao.insertSelective(tborder);
            orderIdList.add(orderId+"");//添加到订单列表
            total_money+=money;//累加到总金额
        }



        /**
         * 3. 根据遍历过程中计算的总价钱, 保存一条支付日志数据
         */
        if("1".equals(order.getPaymentType())){//如果是微信支付
            PayLog payLog=new PayLog();
            String outTradeNo=  idWorker.nextId()+"";//支付订单号
            payLog.setOutTradeNo(outTradeNo);//支付订单号
            payLog.setCreateTime(new Date());//创建时间
            //订单号列表，逗号分隔
            String ids=orderIdList.toString().replace("[", "").replace("]", "").replace(" ", "");
            payLog.setOrderList(ids);//订单号列表，逗号分隔
            payLog.setPayType("1");//支付类型
            payLog.setTotalFee( (long)(total_money*100 ) );//总金额(分)
            payLog.setTradeState("0");//支付状态
            payLog.setUserId(order.getUserId());//用户ID
            payLogDao.insert(payLog);//插入到支付日志表
            //将支付日志(未支付状态), 保存到redis中, 供支付使用
            redisTemplate.boundHashOps(Constants.REDIS_PAYLOG).put(order.getUserId(), payLog);//放入缓存
        }
        /**
         * 4. 保存支付日志, 订单集合, 订单详情集合完成后, 清空redis中的购物车
         */
        redisTemplate.boundHashOps(Constants.REDIS_CARTLIST).delete(order.getUserId());

    }

    @Override
    public PayLog findPayLogFromRedis(String userName) {
        PayLog payLog = (PayLog)redisTemplate.boundHashOps(Constants.REDIS_PAYLOG).get(userName);
        if (payLog == null) {
            payLog = new PayLog();
        }
        return payLog;
    }

    @Override
    public void updateOrderStatus(String out_trade_no, String transaction_id) {
        /**
         * 1.修改支付日志支付状态
         */
        PayLog payLog = payLogDao.selectByPrimaryKey(out_trade_no);
        //支付时间
        payLog.setPayTime(new Date());
        //支付状态改为1, 已支付
        payLog.setTradeState("1");
        //交易号, 微信给我们返回的
        payLog.setTransactionId(transaction_id);
        //更新支付日志到数据库中
        payLogDao.updateByPrimaryKeySelective(payLog);

        /**
         * 2.修改订单支付状态
         */
        String orderList = payLog.getOrderList();//获取订单号列表
        String[] orderIds = orderList.split(",");//获取订单号数组
        if (orderIds != null) {
            for (String orderId : orderIds) {
                Order order = new Order();
                order.setStatus("2");//已付款
                order.setOrderId(Long.parseLong(orderId));
                orderDao.updateByPrimaryKeySelective(order);
            }
        }

        /**
         * 3. 清除redis缓存中的未支付日志数据
         */
        redisTemplate.boundHashOps(Constants.REDIS_PAYLOG).delete(payLog.getUserId());

    }

    @Override
    public List<Order> findAllByName(String name) {
        //根据用户名查询订单
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria r = orderQuery.createCriteria();
        r.andUserIdEqualTo(name);
        List<Order> orders = orderDao.selectByExample(orderQuery);




        //更具订单查询每个订单的 订单详情
        if (orders!=null&& orders.size()>0){
            for (Order order : orders) {
                //通过订单ID查询订单详情  每次循环 更新 条件对象的订单ID
                //订单详情条件查询对象
                OrderItemQuery orderItemQuery = new OrderItemQuery();
                OrderItemQuery.Criteria criteria = orderItemQuery.createCriteria();
                criteria.andOrderIdEqualTo(order.getOrderId());
                List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);
                //遍历订单详情集合 获取每个详情的
                if (orderItems != null &&orderItems.size()>0){
                    order.setOrderItemList(orderItems);
                    for (OrderItem orderItem : orderItems) {

                        Item items = itemDao.selectByPrimaryKey(orderItem.getItemId());
                        orderItem.setItem(items);
                    }

                }
            }
        }
        return orders;
    }

    @Override
    public PageResult search(Order order, int page, int rows) {
        PageHelper.startPage(page, rows);
        //创建查询条件对象
        OrderQuery orderQuery = new OrderQuery();
        //创建where条件对象
        OrderQuery.Criteria criteria = orderQuery.createCriteria();

        if (order!=null){
            //通过订单编号查询
            if (order.getOrderId() !=null && !"".equals(order.getOrderId())){
                criteria.andOrderIdEqualTo(order.getOrderId());
            }
            //通过商家ID查询
            if(order.getSellerId() !=null&& !"".equals(order.getSellerId())){
                criteria.andSellerIdEqualTo(order.getSellerId());
            }

            //更具状态查询
            if (order.getStatus()!=null &&!"".equals(order.getStatus()) ){
                criteria.andStatusEqualTo(order.getStatus());
            }
            //更具用户查询
            if(order.getUserId()!=null && !"".equals(order.getUserId())){
                criteria.andUserIdEqualTo(order.getUserId());
            }
        }

        Page<Order> goodsList = (Page<Order>) orderDao.selectByExample(orderQuery);
        return new PageResult(goodsList.getTotal(), goodsList.getResult());
    }

    @Override
    public List<Order> ExportGood(long[] ids) {
        ArrayList<Order> orders = new ArrayList<>();

        if (ids!=null && ids.length>0){

            for (long id : ids) {
                Order order = orderDao.selectByPrimaryKey(id);
                orders.add(order);
            }


        }
        return orders;
    }


}
