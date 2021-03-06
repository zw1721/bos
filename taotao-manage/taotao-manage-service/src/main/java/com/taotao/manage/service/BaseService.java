package com.taotao.manage.service;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;

@SuppressWarnings("unchecked")
public abstract class BaseService<T extends BasePojo> {
    
    @Autowired
    private Mapper<T> mapper;
    
    // 定义抽象方法，来获取要使用的Mapper。由子类来实现，从而拿到子类对应的Mapper
    // public abstract Mapper<T> getMapper();
    
    private Class<T> clazz;
    
    {
        // 获取父类的参数化类型（泛型）
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        // 获取泛型的实际类型
        clazz = (Class<T>) type.getActualTypeArguments()[0];
    }
    
    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public T queryById(Long id){
        return this.mapper.selectByPrimaryKey(id);
    }
    
    /**
     * 查询所有
     * @return
     */
    public List<T> queryAll(){
        return this.mapper.select(null);
    }
    
    /**
     * 根据条件查询一个
     * @param record
     * @return
     */
    public T queryOne(T record){
        return this.mapper.selectOne(record);
    }
    
    /**
     * 根据条件查询多个
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record){
        return this.mapper.select(record);
    }
    
    /**
     * 分页查询
     * @param record
     * @param page
     * @param rows
     * @return PageInfo
     */
    public PageInfo<T> queryPageListByWhere(T record, Integer page, Integer rows){
        // 分页助手进行分页
        PageHelper.startPage(page, rows);
        List<T> list = this.mapper.select(record);
        // 封装分页对象
        return new PageInfo<>(list);
    }
    /**
     * 分页查询并且排序
     * @param page
     * @param rows
     * @return PageInfo
     */
    public PageInfo<T> queryPageListAndSort(String orderByClause, Integer page, Integer rows){
        // 分页助手进行分页
        PageHelper.startPage(page, rows);
        Example example = new Example(clazz);
        // 排序
        example.setOrderByClause(orderByClause);
        List<T> list = this.mapper.selectByExample(example);
        // 封装分页对象
        return new PageInfo<>(list);
    }
    
    /**
     * 新增数据，包括所有数据
     * @param record
     * @return
     */
    public Integer save(T record){
        // 初始化创建时间和更新时间
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.mapper.insert(record);
    }
    
    /**
     * 新增数据，包括非空数据
     * @param record
     * @return
     */
    public Integer saveSelective(T record){
        // 初始化创建时间和更新时间
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.mapper.insertSelective(record);
    }
    /**
     * 更新数据，包括所有数据
     * @param record
     * @return
     */
    public Integer update(T record){
        // TODO 我们应该判断某些不能更新的字段，是否被更新，如果有，应该抛出异常！
        // 初始化更新时间
        record.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(record);
    }
    
    /**
     * 更新数据，不包括空数据
     * @param record
     * @return
     */
    public Integer updateSelective(T record){
        // 强制把不能更新的字段设置为null
        record.setCreated(null);
        // 初始化更新时间
        record.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(record);
    }
    
    /**
     * 根据主键删除
     * @param id
     * @return
     */
    public Integer deleteById(Long id){
        return this.mapper.deleteByPrimaryKey(id);
    }
    /**
     * 根据多个主键删除
     * @param id
     * @return
     */
    public Integer deleteByIds(String property, List<Object> ids){
        // 创建查询条件
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, ids);
        // 删除
        return this.mapper.deleteByExample(example);
    }
    /**
     * 根据条件删除
     * @param id
     * @return
     */
    public Integer deleteByWhere(T record){
        return this.mapper.delete(record);
    }
}
