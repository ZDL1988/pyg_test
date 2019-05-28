package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.GoodsEntity;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.GoodsService;
import cn.itcast.core.service.PageService;
import cn.itcast.core.service.SolrManagerService;
import cn.itcast.core.util.ExportExcelUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RelationSupport;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 商品管理
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;



//    @Reference
//    private SolrManagerService solrManagerService;
//
//    @Reference
//    private PageService pageService;

    /**
     * 商品分页查询
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody Goods goods, Integer page, Integer rows) {
        //1. 获取当前登录用户的用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //2. 向查询条件对象中添加当前登录用户的用户名作为查询条件
        goods.setSellerId(userName);
        //3. 进行分页查询
        PageResult pageResult = goodsService.search(goods, page, rows);
        return pageResult;
    }
    @RequestMapping("/search2")
    public PageResult search2(@RequestBody Goods goods, Integer page, Integer rows) {
        //1. 获取当前登录用户的用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //2. 向查询条件对象中添加当前登录用户的用户名作为查询条件
        goods.setSellerId(userName);
        //3. 进行分页查询
        PageResult pageResult = goodsService.search2(goods, page, rows);
        return pageResult;
    }

    @RequestMapping("/findOne")
    public GoodsEntity findOne(Long id) {
        GoodsEntity one = goodsService.findOne(id);
        return one;
    }

    /**
     * 商品状态修改
     * @param ids       商品id数组
     * @param status    状态码, 0未审核, 1审核通过, 2驳回
     * @return
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            //1. 到数据库中更新商品的审核状态
            goodsService.updateStatus(ids, status);
            //2. 如果审核通过,
//            if ("1".equals(status) && ids != null) {
//                for (Long goodsId : ids) {
//                    //3. 根据商品id查询数据库商品的详细数据, 然后放入solr索引库中供搜索使用
//                    solrManagerService.addItemToSolr(goodsId);
//                    //4. 根据商品id获取商品详细数据, 根据模板生成商品详情静态化页面
//                    Map<String, Object> rootMap = pageService.findGoodsData(goodsId);
//                    pageService.createStaticPage(goodsId, rootMap);
//                }
//            }
            return new Result(true, "状态修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "状态修改失败!");
        }
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            if (ids != null) {
                for (Long goodsId : ids) {
                    //1. 到数据库中根据商品id, 逻辑删除商品数据
                    goodsService.delete(goodsId);
                    //2.根据商品id删除solr索引库中的库存数据
                    //solrManagerService.deleteItemByGoodsId(goodsId);
                }
            }
            return new Result(true, "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败!");
        }
    }



    @RequestMapping("/Epgood")
    public void  ExportGood(long[] ids,HttpServletResponse response){
        if (ids !=null&& ids.length >0 ){
            //要导出的数据
            List<Goods> goodsList = goodsService.ExportGood(ids);
            //生成的excel 的名字
            String name  ="商品数据"+ UUID.randomUUID().toString().replace("-","");
            // 获取需要转出的excle表头的map字段
            LinkedHashMap<String, String> fieldMap =new LinkedHashMap<String, String>() ;
            fieldMap.put("id", "ID");
            fieldMap.put("sellerId", "商家ID");
            fieldMap.put("goodsName", "spu名");
            fieldMap.put("defaultItemId", "默认sku");
            fieldMap.put("auditStatus", "状态");
            fieldMap.put("isMarketable", "是否上架");
            fieldMap.put("brandId", "品牌ID");
            fieldMap.put("caption", "副标题");
            fieldMap.put("category1Id", "一级类目");
            fieldMap.put("category2Id", "二级类目");
            fieldMap.put("category3Id", "三级类目");
            fieldMap.put("smallPic", "小图");
            fieldMap.put("price", "商城价");
            fieldMap.put("typeTemplateId", "分类模板ID");
            fieldMap.put("isEnableSpec", "是否启用规格");
            fieldMap.put("isDelete", "是否删除");
            ExportExcelUtils.export(name, goodsList, fieldMap, response);
        }else {
            try {
                response.getWriter().write("ID为空或者错误");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
