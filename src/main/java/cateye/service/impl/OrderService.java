package cateye.service.impl;

import cateye.bean.po.Orders;
import cateye.bean.vo.SeatVo;
import cateye.mapper.OrderMapper;
import cateye.service.IOrderService;
import cateye.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class OrderService implements IOrderService {
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private KafkaTemplate<String,String > kafkaTemplate;
    @Resource
    private OrderMapper orderMapper;
    @Override
    public boolean chooseSeat(String watchTimeId, String siteNo, String authorization) {
        // ----------------   组织 Redis中存放座位冻结信息的key  ----------------
        // key的结构 => frozen-{ "watchTimeId" : "xxx" , "seatNo" : "xxxx" }
        // 实例化key的载荷对象
        Map<String,Object> keyPayload = new HashMap<>();
        keyPayload.put( "watchTimeId" , watchTimeId );
        keyPayload.put( "siteNo" , siteNo );
        // 将 key的载荷对象 序列化成 json 字符串
        String key = "frozen-" + JSON.toJSONString( keyPayload );
        // 判断座位是否被冻结
        if( redisUtil.hasKey( key ) ){
            // 当前选择的座位 已经被别的客户 选中了
            return false;
        }
        // 判断座位是否已被购买
        LambdaQueryWrapper<Orders> ordersWrapper = Wrappers.lambdaQuery();
        // => where 1 = 1
        ordersWrapper.eq( Orders::getOrderWtId , watchTimeId );
        // and `order_wt_id` = '202108050347464'
        ordersWrapper.like( Orders::getOrderSites , siteNo);
        // and `order_sites` like '%000000018095-1-5%'
        List<Orders> ordersList = orderMapper.selectList( ordersWrapper );
        // select * from `orders`
        // 判断 当前选择的座位 是否在 数据库orders订单表中 存在
        if( ordersList.size() > 0 ){
            // 当前选择的座位已被购买
            return false;
        }
        // 争夺分布式锁
        if( ! redisUtil.setnx( "mutrix-" + JSON.toJSONString( keyPayload ) , 5 ) ){
            // 没有 争夺到分布式锁 => 抢座失败
            return false;
        }
        redisUtil.set( key , authorization , 60 * 5 );
        return true;
    }


    @Override
    public boolean createOrder(String watchTimeId, List<SeatVo> seatVoList, String authorization) {
        // 座位列表是否为空
        if (seatVoList == null || seatVoList.size() == 0){
            return false;
        }
        // 验证每个座位冻结信息
        for (SeatVo seatVo : seatVoList) {
            String seatNo = seatVo.getSite_no();
            // 生成冻结key
            Map<String,Object> keyPayload = new HashMap<>();
            keyPayload.put( "watchTimeId" , watchTimeId );
            keyPayload.put( "siteNo" , seatNo );
            // 将 key的载荷对象 序列化成 json 字符串
            String key = "frozen-" + JSON.toJSONString( keyPayload );
            // 判断key是否存在
            if(!redisUtil.hasKey(key)){
                return false;
            }
            // 判断是否为该用户冻结的信息
            if (!redisUtil.get(key).toString().equals(authorization)){
                return false;
            }
        }
        // 向MQ中发送订单
        Map<String,String > mqKey = new HashMap<>(8);
        mqKey.put("watchTimeId" , watchTimeId);
        mqKey.put("seatVoList",JSON.toJSONString(seatVoList));
        kafkaTemplate.send("order",JSON.toJSONString(mqKey),authorization);
        return true;
    }
}
