package com.taotao.cart.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;

@Controller
@RequestMapping("api/cart")
public class ApiCartController {

    @Autowired
    private CartService cartService;

    /**
     * 查询购物车列表的接口
     * 
     * @param userId
     * @return
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Cart>> queryCartList(@PathVariable("userId") Long userId) {
        List<Cart> list = this.cartService.queryCartList(userId);
        if (list == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(list);
    }
}
