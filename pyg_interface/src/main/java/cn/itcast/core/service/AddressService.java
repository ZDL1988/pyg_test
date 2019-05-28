package cn.itcast.core.service;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.Areas;
import cn.itcast.core.pojo.address.Cities;
import cn.itcast.core.pojo.address.Provinces;

import java.util.List;

public interface AddressService {

    /**
     * 根据用户查询地址
     * @param userId
     * @return
     */
    public List<Address> findListByUserId(String userId );

    void updatestatus( String name ,Long id);

    List<Provinces> findprovinces();


    List<Cities> findcity(String id);

    List<Areas> findareas(String id);

    void add(Address address, String name);

    void del(Long id, String name);

    Address updateedit(Long id, String name);

    void up(Address address, String name);
}
