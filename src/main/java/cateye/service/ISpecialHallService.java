package cateye.service;

import cateye.bean.po.SpecialHall;

import java.util.List;

public interface ISpecialHallService {
    /**
     * 查询系统中所有放映厅类型
     * @return 放映厅类型实体模型对象集合
     * */
    List<SpecialHall> selectAll();

}
