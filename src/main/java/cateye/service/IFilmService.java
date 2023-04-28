package cateye.service;

import cateye.bean.bo.FilmSearchBo;
import cateye.bean.po.Film;

import java.util.List;

public interface IFilmService {
    /**
     * 根据 筛选条件、搜索条件、排序条件、分页条件，查询满足条件的影片信息
     * @param filmSearchBo 影片搜索业务模型对象
     * @return 影片实体模型对象集合
     */
    List<Film> selectList(FilmSearchBo filmSearchBo);
}
