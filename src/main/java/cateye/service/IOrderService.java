package cateye.service;

import cateye.bean.vo.SeatVo;

import java.util.List;

public interface IOrderService {
    /**
     * 客户选座
     * @param watchTimeId 选择座位的场次编号
     * @param siteNo 选择座位的座位编号
     * @param authorization 选择座位的客户身份令牌
     * @return 选座是否成功
     * */
    boolean chooseSeat( String watchTimeId , String siteNo , String authorization );

    /**
     * 创建订单方法
     * @param watchTimeId 场次编号
     * @param seatVoList 座位视图模型对象集合
     * @param authorization 身份
     * @return 是否成功
     */
    boolean createOrder(String watchTimeId , List<SeatVo> seatVoList,String  authorization );
}
