package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.po.Brand;
import cateye.mapper.BrandMapper;
import cateye.service.IBrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BrandService implements IBrandService {
    @Resource
    private BrandMapper brandMapper;
    @Override
    @RedisCache(duration = 60 * 60 * 24)
    public List<Brand> selectAll() {
        return brandMapper.selectList(null);
    }
}
