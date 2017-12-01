package com.taotao.common.vo;

import java.util.List;

public class EasyUIResult<T> {

    private Long total;// 总条数

    private List<T> rows;// 每页的数据

    public EasyUIResult() {
    }

    public EasyUIResult(Long total, List<T> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
