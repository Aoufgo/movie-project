package cateye.service;

import cateye.bean.po.FilmRegion;
import java.util.List;
public interface IFilmRegionService {
    /**
     * 获取所有电影拍摄地
     * @return 电影拍摄地列表
     */
    List<FilmRegion> selectAll();
}
