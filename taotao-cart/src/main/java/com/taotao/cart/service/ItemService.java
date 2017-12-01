package com.taotao.cart.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.cart.vo.Item;
import com.taotao.common.httpclient.BeanResponseHandler;
import com.taotao.common.service.ApiService;

@Service
public class ItemService {
    
    @Autowired
    private ApiService apiService;
    
    @Value("${TAOTAO_MANAGE_BASE_URL}")
    private String TAOTAO_MANAGE_BASE_URL;
    
    @Value("${TAOTAO_WEB_ITEM_PATH}")
    private String TAOTAO_WEB_ITEM_PATH;
    
    public Item queryItemById(Long itemId){
        String uri = TAOTAO_MANAGE_BASE_URL + TAOTAO_WEB_ITEM_PATH.replace("{itemId}", itemId.toString());
        try {
            return this.apiService.doGet(uri, new BeanResponseHandler<>(Item.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
