package cn.itcast.core.service;

import java.util.List;
import java.util.Map;

public interface EchartsService {

    Map<String, Object> userChart();

    Map<String,List<String>> echartslist();
}
