package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.service.ItemService;
import com.taotao.web.vo.ItemVO;

@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toItem(@PathVariable("itemId") Long itemId) {
        ModelAndView mv = new ModelAndView("item");
        // 查询商品
        ItemVO item = this.itemService.queryItemById(itemId);
        // TODO 错误页面的处理
        // 添加商品到模型
        mv.addObject("item", item);

        // 查询商品描述
        ItemDesc itemDesc = this.itemService.queryItemDescByItemId(itemId);
        // 添加商品描述数据
        mv.addObject("itemDesc", itemDesc);
        return mv;
    }
}
