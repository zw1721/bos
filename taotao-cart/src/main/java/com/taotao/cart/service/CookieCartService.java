package com.taotao.cart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.vo.Item;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;

@Service
public class CookieCartService {

    @Autowired
    private ItemService itemService;

    private static final String COOKIE_NAME = "TT_CART";

    private static final Integer COOKIE_MAXAGE = 3600 * 24 * 180;

    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        // 查询购物车
        Map<Long, Cart> carts = getCarts(request);
        // 获取指定商品
        Cart cart = carts.get(itemId);
        // 判断商品是否存在
        if (cart == null) {
            // 不存在，新增一个商品
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setItemId(itemId);
            cart.setNum(1);// TODO 商品数量不应该走默认

            // 查询商品信息
            Item item = this.itemService.queryItemById(itemId);
            cart.setItemImage(item.getImages()[0]);// TODO 健壮性判断
            cart.setItemPrice(item.getPrice());
            cart.setItemTitle(item.getTitle());
            // 添加
            carts.put(itemId, cart);
        } else {
            // 已存在，修改数量
            cart.setNum(cart.getNum() + 1);// TODO 商品的数量默认为1，应该去页面获取
            cart.setUpdated(new Date());
        }

        setCarts(request, response, carts);
    }

    public List<Cart> queryCartList(HttpServletRequest request) {
        // 查询购物车
        Map<Long, Cart> carts = getCarts(request);
        // TODO 排序
        return new ArrayList<>(carts.values());
    }

    public void updateNum(Long itemId, Integer num, HttpServletRequest request,
            HttpServletResponse response) {
        // 查询购物车
        Map<Long, Cart> carts = getCarts(request);
        // 获取商品
        Cart cart = carts.get(itemId);
        if (cart != null) {
            cart.setNum(num);
            // 写入cookie
            setCarts(request, response, carts);
        }
    }

    public void delete(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        // 查询购物车
        Map<Long, Cart> carts = getCarts(request);
        // 删除
        if (carts.remove(itemId) != null) {
            // 写入cookie
            setCarts(request, response, carts);
        }
    }

    private Map<Long, Cart> getCarts(HttpServletRequest request) {
        // 先查询购物车中的商品
        String json = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        // 反序列化
        Map<Long, Cart> carts;
        try {
            carts = JsonUtils.toMap(json, Long.class, Cart.class);
        } catch (Exception e) {
            // 如果出问题，吧购物车进行初始化
            carts = new HashMap<>();
        }
        return carts;
    }

    private void setCarts(HttpServletRequest request, HttpServletResponse response, Map<Long, Cart> carts) {
        // 把购物车写入cookie
        try {
            CookieUtils.setCookie(request, response, COOKIE_NAME, JsonUtils.toString(carts), COOKIE_MAXAGE,
                    true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
