package cateye.service.impl;

import cateye.bean.bo.UserRegisterBo;
import cateye.bean.po.UserInfo;
import cateye.mapper.UserInfoMapper;
import cateye.service.IUserService;
import cateye.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
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
}
