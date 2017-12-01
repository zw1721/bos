package com.taotao.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.vo.Item;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ItemService itemService;

    public void addItemToCart(Long itemId, Long userId) {
        // 查询购物车商品是否存在
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(userId);
        Cart cart = this.cartMapper.selectOne(record);
        // 判断商品是否存在
        if (cart == null) {
            // 不存在，新增一个商品
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setItemId(itemId);
            cart.setNum(1);// TODO 商品数量不应该走默认
            cart.setUserId(userId);

            // 查询商品信息
            Item item = this.itemService.queryItemById(itemId);
            cart.setItemImage(item.getImages()[0]);// TODO 健壮性判断
            cart.setItemPrice(item.getPrice());
            cart.setItemTitle(item.getTitle());
            // 添加购物车
            this.cartMapper.insert(cart);
        } else {
            // 已存在，修改数量
            cart.setNum(cart.getNum() + 1);// TODO 商品的数量默认为1，应该去页面获取
            cart.setUpdated(new Date());
            // 修改购物车
            this.cartMapper.updateByPrimaryKey(cart);
        }
    }

    public List<Cart> queryCartList(Long userId) {
        Example example = new Example(Cart.class);
        // 查询条件
        example.createCriteria().andEqualTo("userId", userId);
        // 排序
        example.setOrderByClause("created DESC");
        return this.cartMapper.selectByExample(example);
    }

    public void updateNum(Long itemId, Long userId, Integer num) {
        // 要修改的参数
        Cart record = new Cart();
        record.setNum(num);
        record.setUpdated(new Date());
        // 要查询的条件
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("userId", userId).andEqualTo("itemId", itemId);
        this.cartMapper.updateByExampleSelective(record, example);
    }

    public void delete(Long itemId, Long userId) {
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(userId);
        this.cartMapper.delete(record);
    }
}
