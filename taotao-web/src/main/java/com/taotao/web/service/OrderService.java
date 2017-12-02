package com.taotao.web.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.taotao.common.httpclient.BeanResponseHandler;
import com.taotao.common.service.ApiService;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.vo.Order;
@Service
public class OrderService {
	@Autowired
	private ApiService apiService;
	@Value("${ORDER_TAOTAO_BASE_URL}")
	private String ORDER_TAOTAO_BASE_URL;
	public TaotaoResult submit(Order order) {
		try {
			String uri=ORDER_TAOTAO_BASE_URL+"/order/create";
			//将order进序列化
			String json = JsonUtils.toString(order);
			//用httpclient发起订单请求并获取结果，
			return this.apiService.doPostJson(uri, json, new BeanResponseHandler<>(TaotaoResult.class));
		} catch (IOException e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "订单提交失败");
		}
	}
	public Order queryOrderByOrderId(String orderId) {
		String uri=ORDER_TAOTAO_BASE_URL+"/order/query/"+orderId;
		try {
			return this.apiService.doGet(uri, new BeanResponseHandler<>(Order.class));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
