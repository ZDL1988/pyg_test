package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.TemplateService;
import cn.itcast.core.util.ExportExcelUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * 模板管理
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TemplateService templateService;

    /**
     * 高级分页查询
     * @param template  查询条件实体对象
     * @param page      当前页
     * @param rows      每页展示条数
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TypeTemplate template, Integer page, Integer rows) {
        PageResult pageResult = templateService.findPage(template, page, rows);
        return pageResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TypeTemplate template) {
        try {
            templateService.add(template);
            return new Result(true, "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败!");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TypeTemplate template) {
        try{
            templateService.update(template);

            return new Result(true, "修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败!");
        }
    }

    @RequestMapping("/findOne")
    public TypeTemplate findOne(Long id) {
        TypeTemplate one = templateService.findOne(id);
        return one;
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            templateService.delete(ids);

            return new Result(true, "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败!");
        }
    }

    @RequestMapping("/Epmb")
    public void  Epmb(HttpServletResponse response){


        //生成的excel 的名字
        String name  ="模板管理模板"+ UUID.randomUUID().toString().replace("-","");
        // 获取需要转出的excle表头的map字段
        LinkedHashMap<String, String> fieldMap =new LinkedHashMap<String, String>() ;

        fieldMap.put("name", "模板名称");
        fieldMap.put("specIds", "关联规格 填写格式:[1,2,3]");
        fieldMap.put("brandIds", "关联品牌：填写格式:[1,2,3]");
        fieldMap.put("customAttributeItems", "自定义属性：填写格式:[1,2,3]");

        ArrayList<Object> objects = new ArrayList<>();
        ExportExcelUtils.export(name, objects, fieldMap, response);

        try {
            response.getWriter().write("ID为空或者错误");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
