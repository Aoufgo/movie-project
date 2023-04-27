package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.po.Category;
import cateye.mapper.CategoryMapper;
import cateye.service.ICategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    @RedisCache
    public List<Category> selectAll() {
        return categoryMapper.selectList( null);
    }
}
