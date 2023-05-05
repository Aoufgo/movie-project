package cateye.service;

import cateye.bean.po.China;

import java.util.List;

public interface IChinaService {
    /**
     * 根据父级id查询行政地区列表
     * @param parentId
     * @return
     */
    List<China> selectListByParentId(Integer parentId);
}
