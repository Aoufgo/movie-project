package cateye.consumer;

import cateye.bean.po.*;
import cateye.bean.vo.SeatVo;
import cateye.mapper.CinemaMapper;
import cateye.mapper.FilmMapper;
import cateye.mapper.OrderMapper;
import cateye.mapper.WatchTimesMapper;
import cateye.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.weaver.ast.Or;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 订单消费者
 */
@Component
public class OrderConsumer {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private WatchTimesMapper watchTimesMapper;
    @Resource
    private CinemaMapper cinemaMapper;
    @Resource
    private FilmMapper filmMapper;
    @Resource
    private OrderMapper orderMapper;
    /**
     * 订阅order topic 生成订单的消费者
     * @param consumerRecord 消息对象
     * @param ack 回执对象
     */
    @KafkaListener(topics = "order",groupId = "group-01")
    public void create(ConsumerRecord<String,String> consumerRecord, Acknowledgment ack){
        // 解析获取到的信息
        Map<String,String > key = JSON.parseObject(consumerRecord.key(),Map.class);
        String watchTimeId = key.get("watchTimeId");
        List<SeatVo> seatVoList =  JSON.parseArray(key.get("seatVoList"), SeatVo.class);
        String authorization = consumerRecord.value();
        // 获取用户信息
        UserInfo userInfo = (UserInfo) redisUtils.get(authorization);
        // 获取场次信息
        WatchTimes watchTimes = watchTimesMapper.selectById(watchTimeId);
        // 获取影院信息
        Cinema cinema = cinemaMapper.selectById(watchTimes.getCmaId());
        // 获取电影信息
        Film film = filmMapper.selectById(watchTimes.getFilmId());
        // 实例化订单对象
        Orders order = new Orders();
        order.setOrderNo(UUID.randomUUID().toString());
        order.setOrderTime(new Date());
        order.setOrderUserId(userInfo.getUserId());
        order.setOrderUserNick(userInfo.getUserNickName());
        order.setOrderCinemaId(watchTimes.getCmaId());
        order.setOrderCinemaName(cinema.getCmaName());
        order.setOrderFilmId(watchTimes.getFilmId());
        order.setOrderFilmName(film.getFilmName());
        order.setOrderWdDate(watchTimes.getWdDate());
        order.setOrderWtBegintime(watchTimes.getWtBegintime());
        order.setOrderWtEndtime(watchTimes.getWtEndtime());
        order.setOrderWtHalls(watchTimes.getWtHalls());
        order.setOrderCost(watchTimes.getWtCost());
        order.setOrderSites(JSON.toJSONString(seatVoList));
        order.setOrderWtId(watchTimes.getWtId());
        order.setOrderIsUse((byte) 0);
        order.setOrderState((byte) 1);
        // 向db生成订单
        orderMapper.insert(order);
        // 删除订单中冻结信息
        for (SeatVo seatVo : seatVoList) {
            // 生成冻结key
            Map<String,Object> keyPayload = new HashMap<>(4);
            keyPayload.put( "watchTimeId" , watchTimeId );
            keyPayload.put( "siteNo" , seatVo.getSite_no() );
            // 将 key的载荷对象 序列化成 json 字符串
            String fKey = "frozen-" + JSON.toJSONString( keyPayload );
            redisUtils.del(fKey);
        }
        // 提交回执
        ack.acknowledge();
    }
}
