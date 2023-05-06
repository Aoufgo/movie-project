package cateye.controller;

import cateye.response.ResultCodeEnum;
import cateye.response.ResultResponse;
import cateye.utils.RedisUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/watchTimes")
public class WatchTimesController {
    @Resource
    private RedisUtils redisUtils;
    /**
     * 根据场次id获取信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Object watchTimes(@PathVariable String  id, @RequestHeader String authorization){
       //用户身份验证
        if(redisUtils.hasKey(authorization)){
            return ResultResponse.success(null);
        }else {
            return ResultResponse.failure(ResultCodeEnum.UNAUTHORIZED);
        }
    }
}
