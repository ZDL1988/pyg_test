package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.entity.ItemCatEntity;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import cn.itcast.core.util.Constants;
import com.alibaba.dubbo.config.annotation.Service;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.bouncycastle.asn1.x509.X509ObjectIdentifiers.id;

@Service
@Transactional
public class ItemCatServiceImpl implements  ItemCatService {

    @Autowired
    private ItemCatDao catDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        /**
         * 缓存分类数据到redis中
         */
        //1. 查询所有分类表数据
        List<ItemCat> catList = catDao.selectByExample(null);
        if (catList != null) {
            for (ItemCat itemCat : catList) {
                redisTemplate.boundHashOps(Constants.REDIS_CATEGORYLIST).put(itemCat.getName(), itemCat.getTypeId());
            }
        }

        /**
         * 2. 根据父级id进行查询
         */
        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<ItemCat> list = catDao.selectByExample(query);
        return list;
    }

    @Override
    public ItemCat findOne(Long id) {
        return catDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return catDao.selectByExample(null);
    }

    @Override
    public void add(ItemCatEntity itemCatEntity) {
        //新添加分类审核状态为"0"
        itemCatEntity.setAuditStatus("0");
        long id = (long) (Math.random() * 1000000);
        itemCatEntity.setId(id);
        String name = itemCatEntity.getName();
        Long parentId = itemCatEntity.getParentId();
        Long typeId = itemCatEntity.getTypeId();
        ItemCat itemCat = new ItemCat();
        itemCat.setName(name);
        itemCat.setParentId(parentId);
        itemCat.setTypeId(typeId);
        redisTemplate.boundHashOps(Constants.REDIS_ITEMCAT_ENTITY).put(id,itemCatEntity);
        Object o = redisTemplate.boundHashOps(Constants.REDIS_ITEMCAT_ENTITY).get(id);
        System.out.println(o);
    }

    @Override
    public List<ItemCatEntity> findItemCatestatus(String status) {
        //遍历所有的集合
        List<ItemCatEntity> list=new ArrayList<>();
        Map entries = redisTemplate.boundHashOps(Constants.REDIS_ITEMCAT_ENTITY).entries();
        Collection<ItemCatEntity> values = entries.values();

        for (ItemCatEntity itemCatEntity : values) {
            if (status.equals(itemCatEntity.getAuditStatus())){
                list.add(itemCatEntity);
            }
        }
        return list;
    }


}
