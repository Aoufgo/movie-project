package cateye.controller;

import cateye.bean.bo.UserLoginBo;
import cateye.bean.bo.UserRegisterBo;
import cateye.response.ResultResponse;
import cateye.service.IUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    /**
     * 用户注册接口
     * @param userRegisterBo 注册业务模型
     * @return 响应报文
     */
    @PostMapping("/register")
    public Object register(@Valid UserRegisterBo userRegisterBo){
        // 调用 业务逻辑层 实现 客户注册业务功能
        Byte result = userService.register( userRegisterBo );
        // 判断 业务逻辑层 的 调用结果
        if( result == 1 ){
            return ResultResponse.success();
        }else if ( result == -1 ){
            return ResultResponse.failure(501,"验证码校验失败");
        }else if ( result == -2 ){
            return ResultResponse.failure(502,"该手机号已注册过账户");
        }else{
            return ResultResponse.failure(500,"系统错误");
        }
    }

    /**
     * 用户登录
     * @param userLoginBo 登录业务模型
     * @return 响应报文
     */
    @PostMapping("/login")
    public Object login(@Valid UserLoginBo userLoginBo){
        String token = userService.login(userLoginBo);
        if (token == null){
            return ResultResponse.failure(501,"用户名或密码错误!");
        } else {
            return ResultResponse.success(token);
        }
    }
}
