package com.taotao.web.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import com.taotao.web.utils.UserThreadLocal;
import com.taotao.web.vo.ItemVO;
import com.taotao.web.vo.Order;
import com.taotao.web.vo.User;

@Controller
@RequestMapping("order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private UserService userService;
	@RequestMapping(value="{itemId}",method=RequestMethod.GET)
	public ModelAndView toOrder(@PathVariable("itemId")Long itemId){
		ModelAndView mv=new ModelAndView("order");
		//查询商品信息
		ItemVO item = this.itemService.queryItemById(itemId);
		//添加商品信息
		mv.addObject("item", item);
		
		return mv;
	}

	@RequestMapping("submit")
	@ResponseBody
	public TaotaoResult submit(Order order,@CookieValue("TT_TOKEN") String token){
		//获取当前用户的信息
		User user = UserThreadLocal.get();
		order.setUserId(user.getId());
		order.setBuyerNick(user.getUsername());
		//提交订单
		return this.orderService.submit(order);
		
	}
	@RequestMapping(value="success",method=RequestMethod.GET)
	public ModelAndView success(@RequestParam("id")String orderId){
		ModelAndView mv=new ModelAndView("success");
		//封装订单对象
		Order order = this.orderService.queryOrderByOrderId(orderId);
		mv.addObject("order",order);
		
		mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
		return mv;
		
	
	}
}
