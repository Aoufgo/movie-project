package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.po.SpecialHall;
import cateye.mapper.SpecialHallMapper;
import cateye.service.ISpecialHallService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SpecialHallService implements ISpecialHallService {
    @Resource
    private SpecialHallMapper specialHallMapper;
    @Override
    @RedisCache(duration = 60 * 60 * 24)
    public List<SpecialHall> selectAll() {
        return specialHallMapper.selectList(null);
    }
}
