package cateye.bean.vo;

import cateye.bean.po.Film;

import java.util.List;

/**
 * 影院中的影片视图模型类
 */
public class FilmVo {
    /**
     * 一个影片的类型
     */
    private Film film;
    /**
     * film影片在某个影院中最近几天的排片表
     */
    private List<WatchDateVo> watchDateVoList;

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public List<WatchDateVo> getWatchDateVoList() {
        return watchDateVoList;
    }

    public void setWatchDateVoList(List<WatchDateVo> watchDateVoList) {
        this.watchDateVoList = watchDateVoList;
    }
}
