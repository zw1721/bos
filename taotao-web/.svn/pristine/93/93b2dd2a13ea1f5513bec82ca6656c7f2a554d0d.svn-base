package com.taotao.web.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.common.utils.JsonUtils;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.vo.ItemVO;

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_MANAGE_BASE_URL}")
    private String TAOTAO_MANAGE_BASE_URL;

    @Value("${TAOTAO_WEB_ITEM_PATH}")
    private String TAOTAO_WEB_ITEM_PATH;

    @Autowired
    private RedisService redisService;

    private static final String REDIS_ITEM_PREFIX = "TAOTAO_WEB_ITEM_";

    private static final Integer REDIS_ITEM_SECONDS = 3600 * 24 * 30;

    public ItemVO queryItemById(Long itemId) {
        String key = REDIS_ITEM_PREFIX + itemId;
        try {
            // 查询缓存
            String json = this.redisService.get(key);
            if (StringUtils.isNotBlank(json)) {
                return JsonUtils.toBean(json, ItemVO.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 查询商品的路径
        String uri = TAOTAO_MANAGE_BASE_URL + TAOTAO_WEB_ITEM_PATH.replace("{itemId}", itemId.toString());
        try {
            String json = this.apiService.doGet(uri, null);
            if (StringUtils.isBlank(json)) {
                return null;
            }
            try {
                // 把结果加入缓存
                this.redisService.set(key, json, REDIS_ITEM_SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return JsonUtils.toBean(json, ItemVO.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ItemDesc queryItemDescByItemId(Long itemId) {
        try {
            String uri = TAOTAO_MANAGE_BASE_URL + "/rest/api/item/desc/" + itemId;
            String json = this.apiService.doGet(uri, null);
            if (StringUtils.isBlank(json)) {
                return null;
            }
            return JsonUtils.toBean(json, ItemDesc.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteCache(Long itemId) {
        this.redisService.del(REDIS_ITEM_PREFIX + itemId);
    }
}
