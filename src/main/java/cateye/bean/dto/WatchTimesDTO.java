package cateye.bean.dto;
import cateye.bean.po.Cinema;
import cateye.bean.po.Film;
import cateye.bean.po.WatchTimes;

/**
 * 场次信息接口 的 模型转换 类
 * */
public class WatchTimesDTO {

    private WatchTimes watchTimes;      // 场次信息实体模型对象
    private Cinema cinema;              // 该场次所在的影院实体模型对象
    private Film film;                  // 该场次播放的影片实体模型对象

    // getters and setters
    public WatchTimes getWatchTimes() {
        return watchTimes;
    }

    public void setWatchTimes(WatchTimes watchTimes) {
        this.watchTimes = watchTimes;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }
}
