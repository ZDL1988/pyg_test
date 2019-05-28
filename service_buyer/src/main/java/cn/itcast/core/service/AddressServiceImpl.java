package cn.itcast.core.service;

import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.dao.address.AreasDao;
import cn.itcast.core.dao.address.CitiesDao;
import cn.itcast.core.dao.address.ProvincesDao;
import cn.itcast.core.pojo.address.*;
import com.alibaba.dubbo.config.annotation.Service;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    //省
    @Autowired
    private ProvincesDao provincesDao;

    //市
    @Autowired
    private CitiesDao citiesDao;

    //区
    @Autowired
    private AreasDao areasDao;



    /**
     * 获取当前用户所有地址
     * @param userId
     * @return
     */
    @Override
    public List<Address> findListByUserId(String userId) {
        AddressQuery example=new AddressQuery();
        AddressQuery.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return addressDao.selectByExample(example);

    }

    /**
     * 更具ID将此地址设置为默认地址
     * @param id
     */
    @Override
    public void  updatestatus(String name , Long id) {
        //通过ID和用户名查询地址 查看是否有这个地址如果没有抛异常
        AddressQuery addressQuery = new AddressQuery();
        AddressQuery.Criteria criteria = addressQuery.createCriteria();
        criteria.andUserIdEqualTo(name);
        criteria.andIdEqualTo(id);
        List<Address> addresses = addressDao.selectByExample(addressQuery);
        if (addresses != null && addresses.size()>0){
            Address address = addresses.get(0);
            address.setIsDefault("1");
            addressDao.updateByPrimaryKeySelective(address);

            AddressQuery addressQuery2 = new AddressQuery();
            AddressQuery.Criteria criteria2 = addressQuery2.createCriteria();
            criteria2.andIdNotEqualTo(id);
            Address address1 = new Address();
            address1.setIsDefault("0");
            addressDao.updateByExampleSelective(address1,addressQuery2);
        }else {
            throw  new  RuntimeException("ID错误或非法访问");
        }
    }

    @Override
    public List<Provinces> findprovinces() {

        //查询所有的省
        List<Provinces> provinces = provincesDao.selectByExample(null);
        return provinces;

    }

    @Override
    public List<Cities> findcity(String id) {

        //通过是省份ID查询所有城市
        //创建条件查询对象
        CitiesQuery citiesQuery = new CitiesQuery();
        CitiesQuery.Criteria i = citiesQuery.createCriteria();
        i.andProvinceidEqualTo(id);

        List<Cities> cities = citiesDao.selectByExample(citiesQuery);
        return cities;
    }

    @Override
    public List<Areas> findareas(String id) {
        //创建条件查询对象
        AreasQuery areasQuery = new AreasQuery();
        AreasQuery.Criteria criteria = areasQuery.createCriteria();

        criteria.andCityidEqualTo(id);
        List<Areas> areas = areasDao.selectByExample(areasQuery);
        return areas;
    }

    @Override
    public void add(Address address, String name) {
        address.setUserId(name);
        address.setCreateDate(new Date());
        addressDao.insertSelective(address);
    }

    @Override
    public void del(Long id, String name) {
        //通过登录的用户和ID删除地址 只有两个条件满足可以删除

        AddressQuery addressQuery = new AddressQuery();
        AddressQuery.Criteria criteria = addressQuery.createCriteria();
        criteria.andUserIdEqualTo(name);
        criteria.andIdEqualTo(id);
        addressDao.deleteByExample(addressQuery);
    }

    @Override
    public Address updateedit(Long id, String name) {
        AddressQuery addressQuery = new AddressQuery();
        AddressQuery.Criteria criteria = addressQuery.createCriteria();
        criteria.andIdEqualTo(id);
        criteria.andUserIdEqualTo(name);
        List<Address> addressList = addressDao.selectByExample(addressQuery);
        if (addressList != null && addressList.size()>0){
            Address address = addressList.get(0);
           return  address;
        }
        return null;
    }

    @Override
    public void up(Address address, String name) {

        AddressQuery addressQuery = new AddressQuery();
        AddressQuery.Criteria criteria = addressQuery.createCriteria();
        criteria.andUserIdEqualTo(name);
        criteria.andIdEqualTo(address.getId());

        addressDao.updateByExample(address,addressQuery);
    }
}
