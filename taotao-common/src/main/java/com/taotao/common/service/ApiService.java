package com.taotao.common.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * HttpClient的工具类
 * 
 * @author huge
 */
@Service
@SuppressWarnings("unchecked")
public class ApiService {

    @Autowired(required = false)
    private CloseableHttpClient httpClient;

    // 抽取一个通用的String的响应处理器
    private static final ResponseHandler<String> DEFAULT_HANDLER = new BasicResponseHandler();

    /**
     * 无参的GET请求
     * 
     * @param uri
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public <T> T doGet(String uri, ResponseHandler<T> handler) throws ClientProtocolException, IOException {
        handler = (ResponseHandler<T>) (handler == null ? DEFAULT_HANDLER : handler);
        HttpGet httpGet = new HttpGet(uri);
        return httpClient.execute(httpGet, handler);
    }

    /**
     * 有参的GET请求
     * 
     * @param uri
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     * @throws URISyntaxException
     */
    public <T> T doGet(String uri, Map<String, String> params, ResponseHandler<T> handler)
            throws ClientProtocolException, IOException, URISyntaxException {
        // URI的构建器，用来拼接参数
        URIBuilder builder = new URIBuilder(uri);
        // for循环遍历，拼接参数
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.setParameter(entry.getKey(), entry.getValue());
        }
        // 发起请求
        return this.doGet(builder.build().toString(), handler);
    }

    /**
     * 无参的POST请求
     * 
     * @param uri
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public <T> T doPost(String uri, ResponseHandler<T> handler) throws ClientProtocolException, IOException {
        handler = (ResponseHandler<T>) (handler == null ? DEFAULT_HANDLER : handler);
        HttpPost httpPost = new HttpPost(uri);
        return this.httpClient.execute(httpPost, handler);
    }

    /**
     * 有参的POST请求
     * 
     * @param uri
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public <T> T doPost(String uri, Map<String, String> params, ResponseHandler<T> handler)
            throws ClientProtocolException, IOException {
        handler = (ResponseHandler<T>) (handler == null ? DEFAULT_HANDLER : handler);
        HttpPost httpPost = new HttpPost(uri);
        // 根据开源中国的请求需要，设置post请求参数
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        // 构造一个form表单式的实体
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
        // 将请求实体设置到httpPost对象中
        httpPost.setEntity(formEntity);
        return this.httpClient.execute(httpPost, handler);
    }
    
    //有参post请求，提交json
    public <T> T doPostJson(String uri, String json, ResponseHandler<T> handler)
    		throws ClientProtocolException, IOException {
    	handler = (ResponseHandler<T>) (handler == null ? DEFAULT_HANDLER : handler);
    	HttpPost httpPost = new HttpPost(uri);
    	
    	if(json!=null){
    		//构造一个json格式的实体
    		StringEntity stringEntity=new StringEntity(json, ContentType.APPLICATION_JSON);
    		//将实体设置到httpPost实体中
    		httpPost.setEntity(stringEntity);
    	}
    	return this.httpClient.execute(httpPost, handler);
    }
}
