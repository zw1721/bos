package com.taotao.web.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.httpclient.ListResponseHandler;
import com.taotao.common.service.ApiService;
import com.taotao.manage.pojo.Content;

@Service
public class IndexService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_MANAGE_BASE_URL}")
    private String TAOTAO_MANAGE_BASE_URL;

    @Value("${INDEX_AD1_PATH}")
    private String INDEX_AD1_PATH;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 查询大广告数据
     * 
     * @return
     */
    public String queryIndexAD1() {
        // 后台大广告的URL地址
        String uri = TAOTAO_MANAGE_BASE_URL + INDEX_AD1_PATH;
        try {
            // 发起请求获取数据
            List<Content> list = this.apiService.doGet(uri, new ListResponseHandler<>(Content.class));
            // 创建一个Map的集合，保存转换后的数据
            List<Map<String,Object>> result = new ArrayList<>();
            // 遍历Content集合，对数据进行转换
            for (Content c : list) {
                Map<String,Object> map = new LinkedHashMap<>();
                map.put("srcB", c.getPic2());
                map.put("height", 240);
                map.put("alt", c.getTitle());
                map.put("width", 670);
                map.put("src", c.getPic());
                map.put("widthB", 550);
                map.put("href", c.getUrl());
                map.put("heightB", 240);
                result.add(map);
            }
            // 把结果序列化为String
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO 应该准备一套默认的广告
        return null;
    }
}
