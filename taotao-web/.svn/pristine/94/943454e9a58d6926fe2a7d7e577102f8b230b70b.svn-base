package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.IndexService;

@Controller
@RequestMapping("index")
public class IndexController {

    @Autowired
    private IndexService indexService;
    
    /**
     * 跳转到首页
     * @return
     */
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView toIndex(){
        ModelAndView mv = new ModelAndView("index");
        // 查询大广告数据
        String indexAD1 = this.indexService.queryIndexAD1();
        // 加载大广告数据
        mv.addObject("indexAD1", indexAD1);
        return mv;
    }
}
