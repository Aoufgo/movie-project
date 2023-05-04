package cateye.service;

import cateye.bean.po.Brand;

import java.util.List;

public interface IBrandService {
    /**
     * 获取所有品牌
     * @return 品牌列表
     */
    public List<Brand> selectAll();
}
