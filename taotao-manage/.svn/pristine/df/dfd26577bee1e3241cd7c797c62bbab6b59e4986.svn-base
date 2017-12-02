package com.taotao.manage.message.convertor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * 自定义消息转换器
 * @author huge
 *
 */
public class MyJsonpMessageConvertor extends MappingJackson2HttpMessageConverter{

    private String callback;
    
    public MyJsonpMessageConvertor(String callback) {
        this.callback = callback;
    }
    
    @Override
    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        String jsonpFunction = getJsonpFunction();
        // 判断是否有callback
        if(StringUtils.isNotEmpty(jsonpFunction)){
            generator.writeRaw(jsonpFunction + "(");
        }
    }
    @Override
    protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
     String jsonpFunction = getJsonpFunction();
        // 判断是否有callback
        if(StringUtils.isNotEmpty(jsonpFunction)){
            generator.writeRaw(");");
        }
    }
    private String getJsonpFunction() {
        // 从threadLocal中获取当前的Request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String jsonpFunction = request.getParameter(callback);
        return jsonpFunction;
    }
}
