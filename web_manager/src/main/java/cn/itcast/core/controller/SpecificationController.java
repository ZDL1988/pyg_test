package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.entity.SpecEntity;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.service.SpecificationService;
import cn.itcast.core.util.ExportExcelUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 规格管理
 */
@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;


    /**
     * 高级查询
     * @param spec
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody Specification spec, Integer page, Integer rows) {
        PageResult pageResult = specificationService.findPage(spec, page, rows);
        return pageResult;
    }



    /**
     * 规格添加
     * @param specEntity
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody SpecEntity specEntity) {
        try {
            specificationService.add(specEntity);
            return new Result(true, "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败!");
        }
    }

    /**
     * 根据规格id回显
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public SpecEntity findOne(Long id) {
        SpecEntity one = specificationService.findOne(id);
        return one;
    }

    /**
     * 保存修改
     * @param specEntity
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody SpecEntity specEntity) {
        try {
            specificationService.update(specEntity);
            return new Result(true, "修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败!");
        }
    }

    /**
     * 批量删除
     * @param ids   规格id数组
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            specificationService.delete(ids);
            return new Result(true, "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败!");
        }
    }

    /**
     * 加载规格列表, 模板管理中select2下拉框使用
     * @return
     */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        List<Map> maps = specificationService.selectOptionList();
        return maps;
    }

    @RequestMapping("/updatestatus")
    public Result updatestatus(String status, long[] ids){
        try {
            specificationService.updatestatus(status,ids);
            return new Result(true,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,"修改成功");
        }

    }

    @RequestMapping("/Epmb")
    public void  Epmb(HttpServletResponse response){


        //生成的excel 的名字
        String name  ="规格模板"+ UUID.randomUUID().toString().replace("-","");
        // 获取需要转出的excle表头的map字段
        LinkedHashMap<String, String> fieldMap =new LinkedHashMap<String, String>() ;

        fieldMap.put("specName", "规格名称");
        fieldMap.put("specoptionlist", "规格选项");
        fieldMap.put("specoptionlist", "规格选项格式：{[规格选项名称,排序],[规格选项名称,排序]}");

        ArrayList<Object> objects = new ArrayList<>();
        ExportExcelUtils.export(name, objects, fieldMap, response);

        try {
            response.getWriter().write("ID为空或者错误");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
