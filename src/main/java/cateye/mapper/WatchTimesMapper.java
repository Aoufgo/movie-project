package cateye.mapper;

import cateye.bean.po.WatchTimes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * WatchTimes 场次信息数据源 数据访问层 接口
 * */
@Mapper
public interface WatchTimesMapper extends BaseMapper<WatchTimes> {
}
