package com.taotao.manage.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.taotao.common.service.RedisService;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.vo.ItemCatData;
import com.taotao.common.vo.ItemCatResult;
import com.taotao.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat> {
    
    @Autowired
    private RedisService redisService;
    // redis 的缓存时间
    private static final Integer REDIS_SECONDS = 3600 * 24 * 180;
    
    public ItemCatResult queryAllByTree() {
        // 商品类目的缓存的key
        String key = "TAOTAO_MANAGE_ITEM_CAT";
        
        try {
            // 先尝试命中缓存
            String json = this.redisService.get(key);
            // 如果命中，直接返回
            if(StringUtils.isNotEmpty(json)){
                // 反序列化
                return JsonUtils.toBean(json, ItemCatResult.class);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        // 先查询所有
        List<ItemCat> list = this.queryAll();
        // 把数据封装为一个Map，键就是parentId
        Map<Long, List<ItemCat>> map = new HashMap<>();
        for (ItemCat itemCat : list) {
            // 先判断这个类目的pid是否已经存在
            if (!map.containsKey(itemCat.getParentId())) {
                // 如果不存在，创建一个新的ArrayList存储
                map.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            // 把ItemCat存入Map
            map.get(itemCat.getParentId()).add(itemCat);
        }

        // 封装最终结果
        ItemCatResult result = new ItemCatResult();
        // 创建1级类目的集合
        List<ItemCatData> level1List = new ArrayList<>();
        // 把1级类目的集合放到最终结果集中
        result.setItemCats(level1List);

        // 先获取所有的1级类目
        List<ItemCat> itemCats1 = map.get(0L);
        for (ItemCat itemCat : itemCats1) {
            ItemCatData data1 = new ItemCatData();
            level1List.add(data1);
            // 填充url
            data1.setUrl("/products/" + itemCat.getId() + ".html");
            // 填充name
            data1.setName("<a href='/products/" + itemCat.getId() + ".html'>" + itemCat.getName() + "</a>");
            // 填充2级类目
            List<ItemCatData> level2List = new ArrayList<>();
            data1.setItems(level2List);
            // 获取当前1级类目下的所有的2级类目
            List<ItemCat> itemCats2 = map.get(itemCat.getId());
            for (ItemCat itemCat2 : itemCats2) {
                ItemCatData data2 = new ItemCatData();
                level2List.add(data2);
                // 填充url
                data2.setUrl("/products/" + itemCat2.getId() + ".html");
                // 填充name
                data2.setName(itemCat2.getName());
                // 填充3级类目
                List<String> level3List = new ArrayList<>();
                data2.setItems(level3List);
                // 获取当前2级类目下的3级类目
                List<ItemCat> itemCats3 = map.get(itemCat2.getId());
                for (ItemCat itemCat3 : itemCats3) {
                    // 填充3级类目数据
                    level3List.add("/products/"+itemCat3.getId()+".html|" + itemCat3.getName());
                }
            }
            if(level1List.size() > 13){
                break;
            }
        }
        
        try {
            // 在查询数据库以后，我们应该把结果放入缓存。以后就可以命中缓存
            this.redisService.set(key, JsonUtils.toString(result), REDIS_SECONDS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        return result;
    }

    // @Autowired
    // private ItemCatMapper itemCatMapper;

    // public List<ItemCat> queryItemCatByPid(Long pid) {
    // // 准备查询条件
    // ItemCat record = new ItemCat();
    // record.setParentId(pid);
    // // 执行查询
    // return itemCatMapper.select(record);
    // }

    // @Override
    // public Mapper<ItemCat> getMapper() {
    // return itemCatMapper;
    // }
}
