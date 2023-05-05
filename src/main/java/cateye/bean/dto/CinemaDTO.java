package cateye.bean.dto;

import cateye.bean.po.Cinema;
import cateye.bean.vo.FilmVo;

import java.util.List;

public class CinemaDTO {
    /**
     * 一个影院的信息
     */
    private Cinema cinema;
    /**
     * 影院中所有放映影片的排片表
     */
    private List<FilmVo> filmVoList;

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public List<FilmVo> getFilmVoList() {
        return filmVoList;
    }

    public void setFilmVoList(List<FilmVo> filmVoList) {
        this.filmVoList = filmVoList;
    }
}
