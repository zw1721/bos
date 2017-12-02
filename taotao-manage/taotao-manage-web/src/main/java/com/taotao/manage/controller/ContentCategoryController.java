package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@Controller
@RequestMapping("content/category")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService categoryService;

    /**
     * 根据父节点查询子类目集合
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryItemCatByPid(
            @RequestParam(value = "id", defaultValue = "0") Long pid) {
        try {
            // 根据父节点ID查询所有子节点
            ContentCategory record = new ContentCategory();
            record.setParentId(pid);
            List<ContentCategory> list = this.categoryService.queryListByWhere(record);
            if (list == null) {
                // 失败返回404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // 成功返回200
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 新增内容分类
     * 
     * @param category
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory category) {
        try {
            // 初始化其它字段
            category.setId(null);
            category.setIsParent(false);
            category.setSortOrder(1);
            category.setStatus(1);
            this.categoryService.save(category);

            // 判断父节点是否是父
            ContentCategory parent = this.categoryService.queryById(category.getParentId());
            if (!parent.getIsParent()) {
                // 现在还不是父，修改
                parent.setIsParent(true);
                this.categoryService.update(parent);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(category);
        }
    }

    /**
     * 重命名
     * 
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> rename(@RequestParam("id") Long id, @RequestParam("name") String name) {
        try {
            // 组装数据
            ContentCategory record = new ContentCategory();
            record.setId(id);
            record.setName(name);
            this.categoryService.updateSelective(record);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam("id") Long id, @RequestParam("parentId") Long pid) {
        try {
            // 删除
            this.categoryService.delete(id,pid);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
