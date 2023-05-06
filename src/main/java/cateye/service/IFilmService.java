package cateye.service;

import cateye.bean.bo.FilmSearchBo;
import cateye.bean.po.Film;
import cateye.bean.vo.FilmVo;

import java.util.List;

public interface IFilmService {
    /**
     * 根据 筛选条件、搜索条件、排序条件、分页条件，查询满足条件的影片信息
     * @param filmSearchBo 影片搜索业务模型对象
     * @return 影片实体模型对象集合
     */
    List<Film> selectList(FilmSearchBo filmSearchBo);

    /**
     * 根据影片编号查询一个影片信息
     * @param filmId 要查询的影片的编号
     * @return 影片实体模型对象
     */
    Film selectOne(Integer filmId);
    /**
     * 根据影院编号，查询该影院正在上映的影片信息
     * @param cinemaId 要查询的影院编号
     * @return 影片视图模型对象集合
     * */
    List<FilmVo> selectFilmVoListByCinemaId(Integer cinemaId );
}
