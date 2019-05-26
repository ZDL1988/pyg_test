package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;

public interface activeUserService {


    PageResult selectActiveUser(Integer page, Integer rows);

    PageResult selectUnActiveUser(Integer page, Integer rows);
}
