package cateye.service.impl;

import cateye.service.ISMSService;
import cateye.utils.RedisUtils;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SMSService implements ISMSService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${ali-sms.sign-name}")
    private String signName;
    @Value("${ali-sms.register-template-code}")
    private String registerTemplateCode;
    @Resource
    private Client client;
    // 依赖的Redis操作帮助类
    @Resource
    private RedisUtils redisUtil;

    @Override
    public String sendVerify(String phone) {
        // 步骤一：生成6位随机数字的验证码  [ 100000 - 999999 ]
        long code = (long)( Math.random() * 900000 + 100000 );
        // 创建 发送短信的请求对象
        SendSmsRequest request = new SendSmsRequest();
        // 设置接收短信的手机号码
        request.setPhoneNumbers( phone );
        // 设置短信签名（企业申请）
        request.setSignName(signName);
        // 设置短信模板（企业申请）
        request.setTemplateCode(registerTemplateCode);
        // 设置短信模板中的变量参数
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        try {
            // 2.6 发送请求 得到 响应对象
            SendSmsResponse response = client.sendSmsWithOptions( request , new RuntimeOptions());
            ////////////////////////////////////////////////////////////////////////////
            // 判断 短信是否发送成功
            if( "OK".equalsIgnoreCase( response.getBody().getCode() ) ){
                // 短信发送成功
                // 三：将正确的6位数字验证码，存入到Redis中
                redisUtil.set(
                        "SMS-Verify-" + phone,  // 存放的key
                        code,                       // 存放的value
                        60 * 30                     // 数据的有效期（单位：秒）
                );
            } else {
                logger.error("信息发送失败:"+ response.getBody().getMessage());
            }
            // 返回 响应报文体code编码
            return response.getBody().getCode();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }
}
