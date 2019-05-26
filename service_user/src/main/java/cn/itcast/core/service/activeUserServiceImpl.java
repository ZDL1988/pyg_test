package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class activeUserServiceImpl implements activeUserService {
    @Autowired
    private UserDao userDao;

    @Override
    public PageResult selectActiveUser(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);

        UserQuery userQuery = new UserQuery();

        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andStatusEqualTo("Y").andCountsGreaterThanOrEqualTo(3);

        Page<User> pageUser = (Page<User>) userDao.selectByExample(userQuery);

        return new PageResult(pageUser.getTotal(), pageUser.getResult());
    }

    @Override
    public PageResult selectUnActiveUser(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);

        UserQuery userQuery = new UserQuery();

        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andStatusEqualTo("Y").andCountsLessThan(3);

        Page<User> pageUser = (Page<User>) userDao.selectByExample(userQuery);

        return new PageResult(pageUser.getTotal(), pageUser.getResult());
    }
}
