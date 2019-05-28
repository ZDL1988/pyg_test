package cn.itcast.core.controller;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.Areas;
import cn.itcast.core.pojo.address.Cities;
import cn.itcast.core.pojo.address.Provinces;
import cn.itcast.core.service.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: 12778
 * @Date: 2019/5/27 20:13
 * @Description:
 */
@Controller
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    @RequestMapping("/findAll")
    @ResponseBody
    public List<Address> findAll(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Address> listByUserId = addressService.findListByUserId(username);
        return listByUserId;
    }
    @RequestMapping("/updatestatus")
    @ResponseBody
    public String updatestatus(Long id){
        //获取当前登录的用户
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            addressService.updatestatus(name,id);
            return "修改成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "修改失败";
        }
    }

    @RequestMapping("/findprovinces")
    @ResponseBody
    public List<Provinces> findprovinces(){
        List<Provinces> provinces = addressService.findprovinces();
        return provinces;
    }

    @RequestMapping("/findcity")
    @ResponseBody
    public List<Cities> findcity(String id){
        //通过省份ID查询 城市
        List<Cities> citieslist = addressService.findcity(id);
        return citieslist;
    }

    @RequestMapping("/findareas")
    @ResponseBody
    public List<Areas> findareas(String id){
        //通过城市 ID查询  区域
        List<Areas> Areaslist = addressService.findareas(id);
        return Areaslist;
    }

    @RequestMapping("/add")
    @ResponseBody
    public String add( @RequestBody Address pojo){
        //通过城市 ID查询  区域
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();

            addressService.add(pojo,name);
            return "添加成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败";
        }

    }
    @RequestMapping("/del")
    @ResponseBody
    public String del(Long id){
        //通过城市 ID查询  区域
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();

            addressService.del(id,name);
            return "删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "删除失败";
        }

    }

    @RequestMapping("/updateedit")
    @ResponseBody
    public Address updateedit(Long id){
        //通过ID查询地址   用户名作为条件 不能给个ID就可以查询所有用户的地址
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();

            Address address  = addressService.updateedit(id,name);
            return address;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @RequestMapping("/up")
    @ResponseBody
    public String up(@RequestBody Address address){
        //通过ID查询地址   用户名作为条件 不能给个ID就可以查询所有用户的地址
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();

            addressService.up(address,name);
            return "修改成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "修改失败";
        }

    }
}

