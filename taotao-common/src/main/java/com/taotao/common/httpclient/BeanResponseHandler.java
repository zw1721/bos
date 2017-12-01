package com.taotao.common.httpclient;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BeanResponseHandler<T> implements ResponseHandler<T> {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	// 记录EasyUIResult中的类型
	private Class<T> clazz;

	public BeanResponseHandler(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		// 获取响应状态
		final StatusLine statusLine = response.getStatusLine();
		// 获取响应体
		final HttpEntity entity = response.getEntity();
		// 判断是否成功
		if (statusLine.getStatusCode() >= 300) {
			// 不成功抛出异常
			EntityUtils.consume(entity);
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
		// 成功，解析json数据
		String json = entity == null ? null : EntityUtils.toString(entity, "UTF-8");
		
		if(StringUtils.isEmpty(json)){
			return null;
		}
		return MAPPER.readValue(json, clazz);
	}
}