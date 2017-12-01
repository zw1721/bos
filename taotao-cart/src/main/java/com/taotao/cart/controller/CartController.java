package com.taotao.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;
import com.taotao.cart.service.CookieCartService;
import com.taotao.sso.pojo.User;
import com.taotao.web.threadlocal.UserThreadLocal;

@Controller
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CookieCartService cookieCartService;

    /**
     * 添加购物车功能
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") Long itemId, HttpServletRequest request,
            HttpServletResponse response) {
        // 获取当前登录的用户信息
        User user = UserThreadLocal.get();
        // 判断用户是否登录
        if (user == null) {
            // 未登录购物车
            this.cookieCartService.addItemToCart(itemId, request, response);
        } else {
            // 登录的购物车
            this.cartService.addItemToCart(itemId, user.getId());
        }
        return "redirect:/cart/list.html";
    }

    /**
     * 查询购物车列表
     * 
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView toCartList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("cart");
        // 获取当前登录的用户信息
        User user = UserThreadLocal.get();
        List<Cart> cartList = null;
        // 判断用户是否登录
        if (user == null) {
            // 未登录购物车
            cartList = this.cookieCartService.queryCartList(request);
        } else {
            // 登录的购物车
            cartList = this.cartService.queryCartList(user.getId());
        }
        // 吧购物车信息添加到模型
        mv.addObject("cartList", cartList);
        // 查询购物车信息
        return mv;
    }

    /**
     * 修改购物车数量
     * 
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateNum(@PathVariable("itemId") Long itemId,
            @PathVariable("num") Integer num, HttpServletRequest request, HttpServletResponse response) {
        // 获取当前登录的用户信息
        User user = UserThreadLocal.get();
        // 判断用户是否登录
        if (user == null) {
            // 未登录购物车
            this.cookieCartService.updateNum(itemId, num, request, response);
        } else {
            // 登录的购物车
            this.cartService.updateNum(itemId, user.getId(), num);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除购物车商品
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String delete(@PathVariable("itemId") Long itemId, HttpServletRequest request,
            HttpServletResponse response) {
        // 获取当前登录的用户信息
        User user = UserThreadLocal.get();
        // 判断用户是否登录
        if (user == null) {
            // 未登录购物车
            this.cookieCartService.delete(itemId, request, response);
        } else {
            // 登录的购物车
            this.cartService.delete(itemId, user.getId());
        }
        return "redirect:/cart/list.html";
    }
}
