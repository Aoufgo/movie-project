package cateye.controller;

import cateye.service.ISMSService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cateye.response.ResultResponse;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sms")
public class SMSController {
    @Resource
    private ISMSService smsService;
    @GetMapping("/verify/{phone}")
    public Object verify(@PathVariable String phone){
        String code = smsService.sendVerify(phone);
        if( "OK".equals(code) ){
            // 发送短信验证码成功
            return ResultResponse.success(code);
        }else{
            // 发送短信验证码失败
            return ResultResponse.failure(500,"短信验证码发送失败");
        }
    }
}
