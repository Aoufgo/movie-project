package cateye.service;

import cateye.bean.bo.UserLoginBo;
import cateye.bean.bo.UserRegisterBo;

public interface IUserService {
    /**
     * 用户注册业务
     * @param userRegisterBo 业务模型
     * @return 客户注册是否成功。 1：注册成功 -1：验证码校验失败  -2：该手机号已注册过账户
     */
    Byte register(UserRegisterBo userRegisterBo);

    /**
     * 用户登录业务
     * @param userLoginBo 业务模型
     * @return 签发的身份令牌 null则表示登录失败
     */
    String login(UserLoginBo userLoginBo);
}
