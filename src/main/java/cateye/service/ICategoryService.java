package cateye.service;

import cateye.bean.po.Category;

import java.util.List;

public interface ICategoryService {
    /**
     * 查询所有分类
     * @return 分类列表
     */
    List<Category> selectAll();
}
