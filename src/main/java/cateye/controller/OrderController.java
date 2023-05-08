package cateye.controller;

import cateye.bean.dto.WatchTimesDTO;
import cateye.bean.po.WatchTimes;
import cateye.bean.vo.SeatVo;
import cateye.response.ResultCodeEnum;
import cateye.response.ResultResponse;
import cateye.service.impl.OrderService;
import cateye.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private OrderService orderService;
    /**
     * 选择座位
     * @param watchTimeId 场次id
     * @param siteNo 座位id
     * @return
     */
    @PostMapping("/chooseSeat")
    public Object chooseSeat(String watchTimeId, String siteNo, @RequestHeader String authorization){
        //用户身份验证
        if(redisUtils.hasKey(authorization)){
            // 选座
            if (orderService.chooseSeat(watchTimeId,siteNo,authorization)){
                return ResultResponse.success();
            }else {
                return ResultResponse.failure(500,"该座位已被别的客户占用，请重新选择");
            }
        }else {
            return ResultResponse.failure(ResultCodeEnum.UNAUTHORIZED);
        }

    }

    /**
     * 创建订单接口
     * @param watchTimeId 场次id
     * @param seatList 座位json
     * @param authorization 用户
     * @return 报文
     */

    @PutMapping
    public Object createOrder(
            String watchTimeId,
            String seatList,
            @RequestHeader String authorization
    ){
        //用户身份验证
        if(redisUtils.hasKey(authorization)){
            if(orderService.createOrder(watchTimeId, JSON.parseArray(seatList, SeatVo.class),authorization)){
                return ResultResponse.success();
            }else {
                return ResultResponse.failure(500,"订单生成失败");
            }
        }else {
            return ResultResponse.failure(ResultCodeEnum.UNAUTHORIZED);
        }
    }
}
