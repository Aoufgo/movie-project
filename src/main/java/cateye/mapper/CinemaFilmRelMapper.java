package cateye.mapper;

import cateye.bean.po.CinemaFilmRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * CinemaFilmRel 影院和影片关系数据源 数据访问层 接口
 * */
@Mapper
public interface CinemaFilmRelMapper extends BaseMapper<CinemaFilmRel> {
}
