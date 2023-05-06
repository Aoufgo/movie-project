package cateye.service;

import cateye.bean.bo.CinemaSearchBo;
import cateye.bean.po.Cinema;
import cateye.bean.vo.FilmVo;

import java.util.List;

public interface ICinemaService {
    /**
     * 根据筛选条件、搜索条件、排序条件、分页条件，查询满足条件的影院信息
     * @param cinemaSearchBo 影院搜索业务模型对象
     * @return 影院实体模型对象集合
     * */
    List<Cinema> selectList(CinemaSearchBo cinemaSearchBo );

    /**
     * 根据影院编号，从es中查询一个影院信息
     * @param cinemaId 要查询的影院编号
     * @return 影院实体模型对象
     * */
    Cinema selectOne( Integer cinemaId );
    /**
     * 根据影院编号，从数据库查询一个影院信息
     * @param cinemaId 要查询的影院编号
     * @return 影院实体模型对象
     * */
    Cinema selectOneFromDB( Integer cinemaId );

}
