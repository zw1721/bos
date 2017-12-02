package com.taotao.web.vo;

import org.apache.commons.lang3.StringUtils;

import com.taotao.manage.pojo.Item;

public class ItemVO extends Item{

    /**
     * 给页面视图提供一个新的属性
     * @return
     */
    public String[] getImages(){
        return StringUtils.split(this.getImage(), ",");
    }
}
