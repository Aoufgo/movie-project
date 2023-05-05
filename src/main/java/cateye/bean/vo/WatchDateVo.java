package cateye.bean.vo;

import cateye.bean.po.WatchTimes;

import java.util.List;

/**
 * 观影日期的视图模型类
 */
public class WatchDateVo {
    /**
     * 日期
     */
    private String date;
    /**
     * date日期中某个影院的排片表
     */
    private List<WatchTimes> watchTimeList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<WatchTimes> getWatchTimeList() {
        return watchTimeList;
    }

    public void setWatchTimeList(List<WatchTimes> watchTimeList) {
        this.watchTimeList = watchTimeList;
    }
}
