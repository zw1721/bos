package com.taotao.manage.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

@Controller
@RequestMapping("api/content")
public class ApiContentController {
    
    @Autowired
    private ContentService contentService;
    
    /**
     * 根据内容分类查询内容的的列表
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<Content>> queryContentList(@RequestParam("categoryId") Long categoryId){
        try {
            Content record = new Content();
            record.setCategoryId(categoryId);
            List<Content> list = this.contentService.queryListByWhere(record);
            if(list == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
