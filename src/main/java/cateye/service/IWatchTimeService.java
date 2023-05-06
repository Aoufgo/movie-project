package cateye.service;

import cateye.bean.po.WatchTimes;

public interface IWatchTimeService {
    /**
     * 根据场次编号,查询一个场次信息
     * @param watchTimesId id
     * @return 实体
     */
    WatchTimes selectOne(String watchTimesId);
}
