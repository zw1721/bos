package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.EasyUIResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

@Controller
@RequestMapping("content")
public class ContentController {
    
    @Autowired
    private ContentService contentService;
    
    /**
     * 新增内容的接口
     * @param content
     * @return
     */
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Void> saveContent(Content content){
        try {
            // 数据校验
            if(content.getCategoryId() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            content.setId(null);
            this.contentService.save(content);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * 分页查询内容
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<EasyUIResult<Content>> queryContentList(@RequestParam("categoryId") Long categoryId,
            @RequestParam(value="page",defaultValue = "1")Integer page,
            @RequestParam(value="rows",defaultValue = "10")Integer rows
            ){
        try {
            // TODO 应该对查询的结果进行排序
            Content record = new Content();
            record.setCategoryId(categoryId);
            PageInfo<Content> pageInfo = this.contentService.queryPageListByWhere(record, page, rows);
            if(pageInfo == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(new EasyUIResult<>(pageInfo.getTotal(), pageInfo.getList()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
