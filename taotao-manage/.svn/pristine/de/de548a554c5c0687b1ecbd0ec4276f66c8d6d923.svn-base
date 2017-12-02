package com.taotao.manage.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.vo.ItemCatResult;
import com.taotao.manage.service.ItemCatService;

@Controller
@RequestMapping("api/item/cat")
public class ApiItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    // jackson-json的序列化工具
    // private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 根据父节点查询子类目集合
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryAllItemCat() {
        try {
            ItemCatResult result = this.itemCatService.queryAllByTree();
            if (result == null) { // 失败返回404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } // 成功返回200
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // @RequestMapping(method = RequestMethod.GET)
    // public ResponseEntity<String> queryAllItemCat(@RequestParam("callback") String callback) {
    // try {
    // ItemCatResult result = this.itemCatService.queryAllByTree();
    // if (result == null) {
    // // 失败返回404
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    // }
    // // 把对象序列化为字符串
    // String msg = MAPPER.writeValueAsString(result);
    // if (StringUtils.isNotEmpty(callback)) {
    // // 跨域
    // msg = callback + "(" + msg + ");";
    // }
    // // 成功返回200
    // return ResponseEntity.ok(msg);
    // } catch (Exception e) {
    // e.printStackTrace();
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // }
    // }

}
