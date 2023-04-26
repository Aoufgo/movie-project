package cateye.service.impl;

import cateye.bean.po.FilmRegion;
import cateye.mapper.FilmRegionMapper;
import cateye.service.IFilmRegionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class FilmRegionService implements IFilmRegionService {
    @Resource
    private FilmRegionMapper filmRegionMapper;
    @Override
    public List<FilmRegion> selectAll() {
        return filmRegionMapper.selectList(null);
    }
}
