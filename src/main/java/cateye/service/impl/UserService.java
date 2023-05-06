package cateye.service.impl;

import cateye.bean.bo.UserLoginBo;
import cateye.bean.bo.UserRegisterBo;
import cateye.bean.po.UserInfo;
import cateye.mapper.UserInfoMapper;
import cateye.service.IUserService;
import cateye.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisUtils redisUtils;
    @Override
    public Byte register(UserRegisterBo userRegisterBo) {
        // 验证码校验
        String correctVerify = redisUtils.get("SMS-Verify-" + userRegisterBo.getUserLoginName()).toString();
        if (!correctVerify.equals(userRegisterBo.getVerifyCode())){
            return -1;
        }
        // 手机是否注册
        LambdaQueryWrapper<UserInfo> queryWrapper= Wrappers.lambdaQuery();
        queryWrapper.eq(UserInfo::getUserLoginName,userRegisterBo.getUserLoginName());
        if(userInfoMapper.exists(queryWrapper)){
             return -2;
        }
        // 实例化用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserLoginName(userRegisterBo.getUserLoginName());
        // 密码盐值
        userInfo.setUserSalt(UUID.randomUUID().toString());
        // 账户密码明文第一次MD5加密
        String password = DigestUtils.md5DigestAsHex( userRegisterBo.getUserLoginPass().getBytes() );
        // 密码加盐
        String[] arr = userInfo.getUserSalt().split("-");
        password = arr[0] + arr[1] + password + arr[2] + arr[3] + arr[4];
        // 账户密码加盐后的第二次MD5加密
        password = DigestUtils.md5DigestAsHex( password.getBytes() );
        // 配置 账户密码
        userInfo.setUserLoginPass( password );
        // 昵称
        userInfo.setUserNickName("用户"+ UUID.randomUUID().toString().substring(0,6));
        // 状态
        userInfo.setUserEnable((byte) 1);
        // 添加用户
        userInfoMapper.insert(userInfo);
        return 1;
    }

    @Override
    public String login(UserLoginBo userLoginBo) {
        // 名称验证
        LambdaQueryWrapper<UserInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserInfo::getUserLoginName,userLoginBo.getUserLoginName());
        List<UserInfo> userInfoList = userInfoMapper.selectList(queryWrapper);
        if (userInfoList.size() == 0){
            return null;
        }
        UserInfo userInfo = userInfoList.get(0);
        // 密码验证
        // 账户密码明文第一次MD5加密
        String password = DigestUtils.md5DigestAsHex( userLoginBo.getUserLoginPass().getBytes() );
        // 密码加盐
        String[] arr = userInfo.getUserSalt().split("-");
        password = arr[0] + arr[1] + password + arr[2] + arr[3] + arr[4];
        // 账户密码加盐后的第二次MD5加密
        password = DigestUtils.md5DigestAsHex( password.getBytes() );
        if (!userInfo.getUserLoginPass().equals(password)){
            return null;
        }
        // 签发token令牌
        String token = JWT.create().withAudience(JSON.toJSONString(userInfo))
                // 载荷 令牌的签发时间（当前时间）
                .withIssuedAt(new Date())
                // 载荷 令牌的到期时间（24分钟以后）
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                // 使用秘钥签发令牌
                .sign(Algorithm.HMAC256(userInfo.getUserId().toString()));
        // token存入redis
        redisUtils.set("Bearer " + token, userInfo, 60 * 24);
        // 返回token
        return token;
    }
}
