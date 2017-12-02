package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;

@Controller
@RequestMapping("item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 根据父节点查询子类目集合
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCatByPid(
            @RequestParam(value = "id", defaultValue = "0") Long pid) {
        try {
            // 根据父节点ID查询所有子节点
            ItemCat record = new ItemCat();
            record.setParentId(pid);
            List<ItemCat> list = this.itemCatService.queryListByWhere(record);
            if (list == null) {
                // 失败返回404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // 成功返回200
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
