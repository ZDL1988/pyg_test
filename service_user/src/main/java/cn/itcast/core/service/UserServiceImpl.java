package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    //点对点发送, 验证码到这个目标中, 队列名称叫做sms
    @Autowired
    private ActiveMQQueue smsDestination;

    @Autowired
    private UserDao userDao;

    //模板编号
    @Value("${template_code}")
    private String templateCode;

    //签名
    @Value("${sign_name}")
    private String singName;


    @Override
    public void sendCode(final String phone) {

        //1. 生成一个随机六位以内的数字作为短信验证码
        final long code = (long) (Math.random() * 1000000);
        //2. 将手机号作为key, 验证码作为value保存到redis, 生存时间为10分钟
        redisTemplate.boundValueOps(phone).set(code, 10, TimeUnit.MINUTES);
        //3. 将手机号, 验证码, 模板编号, 签名等内容封装成Map类型的消息, 发送给消息服务器
        //消息服务器在这里起到高并发的时候的缓冲功能.
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //创建Map类型的消息对象
                MapMessage mapMessage = session.createMapMessage();
                //模板编号
                mapMessage.setString("templateCode", templateCode);
                //签名
                mapMessage.setString("singName", singName);
                //手机号
                mapMessage.setString("phone", phone);

                //封装短信内容, 短信内容必须是json格式, 这里放入map中使用工具转换成json
                Map<String, String> contentMap = new HashMap<>();
                contentMap.put("code", String.valueOf(code));
                //短信内容
                mapMessage.setString("content", JSON.toJSONString(contentMap));
                return mapMessage;
            }
        });


    }

    @Override
    public boolean checkCode(String phone, String smsCode) {
        //1. 校验手机号和验证码不为空
        if (phone == null || "".equals(phone) || smsCode == null || "".equals(smsCode)) {
            return false;
        }
        //2. 根据手机号到redis中获取验证码
        Long redisSmsCode = (Long) redisTemplate.boundValueOps(phone).get();
        //3. 判断如果获取不到验证码直接返回false校验失败
        if (redisSmsCode == null || "".equals(redisSmsCode)) {
            return false;
        }
        //4. 判断页面传入的验证码是否等于我们redis自己保存的验证码
        if (smsCode.equals(String.valueOf(redisSmsCode))) {
            //5. 校验验证码正确, 返回正确信息并且将redis中对应的验证码删除
            redisTemplate.delete(phone);
            return true;
        }
        return false;
    }

    @Override
    public void add(User user) {
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //新添加的用户都是正常用户
        user.setStatus("Y");
        userDao.insertSelective(user);
    }


    @Override
    public void add(User user, String smscode) {
        //判断验证码是否正确
        //从缓存中获取验证码
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        //首先判断是否为空 因为如果时间到了 验证码被清空就会失效
        if (null != code) {
            //在进行判断验证码是否相等
            if (code.equals(smscode)) {
                //如果验证码相同 就添加到数据库
                //将数据表中的非空字段给赋值
                user.setCreated(new Date());
                user.setUpdated(new Date());
                user.setStatus("0");
                userDao.insertSelective(user);
            } else {
                throw new RuntimeException("验证码错误");
            }
        } else {
            throw new RuntimeException("验证码已失效");
        }
    }

    public static void main(String[] args) {
        long random = (long) (Math.random() * 1000000);
        System.out.println("=======" + random);
    }


    @Override
    public List<User> findAll() {
        return userDao.selectByExample(null);
    }

    @Override
    public PageResult search(Integer page, Integer rows, User user) {
        PageHelper.startPage(page, rows);
        Page<User> page1 = (Page<User>) userDao.selectByExample(null);
        return new PageResult(page1.getTotal(), page1.getResult());
    }

    @Override
    public void freeze(Long[] ids) {
        for (Long id : ids) {
            //修改状态 冻结
            User user = userDao.selectByPrimaryKey(id);
            user.setStatus("N");
            userDao.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    public void unfreeze(Long[] ids) {
        for (Long id : ids) {
            //修改状态 解冻
            User user = userDao.selectByPrimaryKey(id);
            user.setStatus("Y");
            userDao.updateByPrimaryKeySelective(user);
        }
    }

    @Override
    public void update(String username) {
        User user = userDao.selectUserByUserName(username);
        user.setLastLoginTime(new Date());
        user.setCounts(user.getCounts() + 1);
        userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public User selectByUsername(String username) {
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andStatusEqualTo("Y").andUsernameEqualTo(username);
        List<User> users = userDao.selectByExample(userQuery);
        if (null != users && users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

}
