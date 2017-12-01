package com.taotao.common.httpclient;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 创建一个集合的响应处理器
 * @author huge
 * @param <T>
 */
public class ListResponseHandler<T> implements ResponseHandler<List<T>>{

    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private Class<T> clazz;// 集合中的元素类型
    
    // 通过构造函数来指定集合中的元素的类型
    public ListResponseHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        String json = entity == null ? null : EntityUtils.toString(entity,"UTF-8");
        return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    }

}
