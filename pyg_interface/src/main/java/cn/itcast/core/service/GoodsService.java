package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.GoodsEntity;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;

import java.util.List;

public interface GoodsService {

    public PageResult search(Goods goods, Integer page, Integer rows);

    public void add(GoodsEntity goodsEntity);

    public GoodsEntity findOne(Long id);

    public  void  update(GoodsEntity goodsEntity);

    public void delete(Long id);

    public void updateStatus(Long[] ids, String status);

    List<Goods> ExportGood(long[] ids);

    PageResult search2(Goods goods, Integer page, Integer rows);
}
