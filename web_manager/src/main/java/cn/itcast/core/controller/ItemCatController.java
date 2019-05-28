package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import cn.itcast.core.util.ExportExcelUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

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

    @RequestMapping("/findAll")
    public List<ItemCat> findAll() {
        List<ItemCat> all = catService.findAll();
        return all;
    }
    @RequestMapping("/Epmb")
    public void  Epmb(HttpServletResponse response){


        //生成的excel 的名字
        String name  ="分类管理模板"+ UUID.randomUUID().toString().replace("-","");
        // 获取需要转出的excle表头的map字段
        LinkedHashMap<String, String> fieldMap =new LinkedHashMap<String, String>() ;

        fieldMap.put("parentId", "上级id");
        fieldMap.put("name", "类目名称");
        fieldMap.put("typeId", "模板类型id");

        ArrayList<Object> objects = new ArrayList<>();
        ExportExcelUtils.export(name, objects, fieldMap, response);

        try {
            response.getWriter().write("ID为空或者错误");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
