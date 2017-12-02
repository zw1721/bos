package com.taotao.manage.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;

@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 新增商品
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc) {
        try {
            // 一定要做数据校验。 示范一个
            if (StringUtils.isEmpty(item.getTitle())) {
                // 参数有误，返回400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            this.itemService.saveItem(item, desc);
            // 返回201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            // 返回500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * 修改商品
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc) {
        try {
            // 一定要做数据校验。 示范一个
            if (StringUtils.isEmpty(item.getTitle())) {
                // 参数有误，返回400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            this.itemService.updateItem(item, desc);
            // 返回204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
            // 返回500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 分页查询商品
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<EasyUIResult<Item>> queryItemList(
            @RequestParam(value="page",defaultValue = "1")Integer page,
            @RequestParam(value="rows",defaultValue = "10")Integer rows
            ){
        try {
            PageInfo<Item> pageInfo = this.itemService.queryPageListAndSort("created DESC", page, rows);
            if(pageInfo != null){
                return ResponseEntity.ok(new EasyUIResult<>(pageInfo.getTotal(), pageInfo.getList()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
