package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.po.China;
import cateye.mapper.ChinaMapper;
import cateye.service.IChinaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChinaService implements IChinaService {
    @Resource
    private ChinaMapper chinaMapper;

    @Override
    @RedisCache(duration = 60 * 60 * 24)
    public List<China> selectListByParentId(Integer parentId) {
        LambdaQueryWrapper<China> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(China::getPid,parentId);
        return chinaMapper.selectList(queryWrapper);
    }
}
