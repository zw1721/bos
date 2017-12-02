package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.ContentCategory;

@Service
public class ContentCategoryService extends BaseService<ContentCategory>{

    public void delete(Long id, Long pid) {
        // 先查询当前节点
        ContentCategory category = this.queryById(id);
        // 定义集合，存储待删除id
        List<Object> ids = new ArrayList<>();
        // 调用递归方法，获取所有待删除的ID
        getIds(ids,category);
        // 开始删除
        this.deleteByIds("id", ids);
        
        // 判断当前节点的父节点，是否还有子节点
        ContentCategory record = new ContentCategory();
        record.setParentId(pid);
        List<ContentCategory> list = this.queryListByWhere(record);
        if(list == null || list.size() == 0){
            record.setParentId(null);
            // 设置ID
            record.setId(pid);
            // 设置isparent
            record.setIsParent(false);
            // 证明没有儿子
            this.updateSelective(record);
        }
        
        // TODO 应该把分类下的所有内容也删除
    }

    private void getIds(List<Object> ids, ContentCategory category) {
        // 先把当前节点放到集合
        ids.add(category.getId());
        // 判断是否有儿子
        if(category.getIsParent()){
            // 查所有儿子
            ContentCategory record = new ContentCategory();
            record.setParentId(category.getId());
            List<ContentCategory> list = this.queryListByWhere(record);
            for (ContentCategory c : list) {
                // 递归
                getIds(ids,c);
            }
        }
    }

}
