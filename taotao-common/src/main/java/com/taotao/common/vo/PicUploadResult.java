package com.taotao.common.vo;

/**
 * KindEditor上传文件结果对象
 */
public class PicUploadResult {

    private Integer error;// 0：成功， 1：失败

    private String url;// 地址

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
