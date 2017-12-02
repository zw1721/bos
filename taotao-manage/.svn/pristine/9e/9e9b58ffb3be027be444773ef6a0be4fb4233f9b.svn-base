package com.taotao.manage.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.service.ApiService;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;

@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemDescService itemDescService;
    
    @Autowired
    private ApiService apiService;

    public void saveItem(Item item, String desc) {
        // 不能添加的字段，强制设为null
        item.setId(null);
        // 初始化商品的状态
        item.setStatus(1);
        // 添加商品
        this.save(item);

        // 封装商品的描述信息
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        this.itemDescService.save(itemDesc);
    }

    public void updateItem(Item item, String desc) {
        // 初始化商品的状态
        item.setStatus(null);
        // 添加商品
        this.updateSelective(item);

        // 封装商品的描述信息
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        this.itemDescService.update(itemDesc);

        // 通知前台系统，缓存需要删除了
        String uri = "http://www.taotao.com/cache/" + item.getId() + ".html";
        try {
            this.apiService.doPost(uri, null);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO 这里可以尝试失败后重试
        }
    }
}
