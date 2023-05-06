package cateye.controller;

import cateye.bean.dto.WatchTimesDTO;
import cateye.bean.po.WatchTimes;
import cateye.response.ResultCodeEnum;
import cateye.response.ResultResponse;
import cateye.service.impl.CinemaService;
import cateye.service.impl.FilmService;
import cateye.service.impl.WatchTimesService;
import cateye.utils.RedisUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/watchTimes")
public class WatchTimesController {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private WatchTimesService watchTimesService;
    @Resource
    private CinemaService cinemaService;
    @Resource
    private FilmService filmService;
    /**
     * 根据场次id获取信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Object watchTimes(@PathVariable String  id, @RequestHeader String authorization){
       //用户身份验证
        if(redisUtils.hasKey(authorization)){
            WatchTimesDTO watchTimesDTO = new WatchTimesDTO();
            WatchTimes watchTimes = watchTimesService.selectOne(id);
            watchTimesDTO.setWatchTimes(watchTimes);
            if (watchTimes != null){
                watchTimesDTO.setFilm(filmService.selectOneFromDB(watchTimes.getFilmId()));
                watchTimesDTO.setCinema(cinemaService.selectOneFromDB(watchTimes.getCmaId()));
            }
            return ResultResponse.success(watchTimesDTO);
        }else {
            return ResultResponse.failure(ResultCodeEnum.UNAUTHORIZED);
        }
    }
}
