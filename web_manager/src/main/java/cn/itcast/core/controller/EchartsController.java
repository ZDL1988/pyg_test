package cn.itcast.core.controller;

import cn.itcast.core.service.EchartsService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class EchartsController {
    @Reference
    private EchartsService echartsService;

    @RequestMapping("/userChart")
    public  Map<String,Object> userChart() throws JsonProcessingException {
        return echartsService.userChart();
//        String s = new ObjectMapper().writeValueAsString(stringListMap);
//        return s;
    }

    @RequestMapping("/echartslist")
    public Map<String,List<String>> echartslist(){
        return echartsService.echartslist();
    }
}
