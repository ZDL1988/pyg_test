package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.ItemCatEntity;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService catService;

    /**
     * 根据父级id查询所有子集
     * @param parentId
     * @return
     */
    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId) {
        List<ItemCat> list = catService.findByParentId(parentId);
        return list;
    }

    @RequestMapping("/findOne")
    public ItemCat findOne(Long id) {
        ItemCat one = catService.findOne(id);
        return one;
    }

    @RequestMapping("/findAll")
    public List<ItemCat> findAll() {
        List<ItemCat> all = catService.findAll();
        return all;
    }



    /**
     * 添加
     * @param itemCatEntity 添加的品分类对象
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody ItemCatEntity itemCatEntity) {
        try {
            catService.add(itemCatEntity);
            return new Result(true, "保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败!");
        }
    }


    @RequestMapping("/findItemCatestatus")
  public List<ItemCatEntity> findItemCatestatus(String status){
        List<ItemCatEntity> list= catService.findItemCatestatus(status);
        return list;

  }









}
