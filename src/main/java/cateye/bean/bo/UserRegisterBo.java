package cateye.bean.bo;


import javax.validation.constraints.NotBlank;

/**
 * 用户注册业务模型类
 */
public class UserRegisterBo {
    @NotBlank(message = "用户名或手机号不能为空!")
    private String userLoginName;
    @NotBlank(message = "密码不能为空!")
    private String userLoginPass;
    @NotBlank(message = "验证码不能为空!")
    private String verifyCode;

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }

    public String getUserLoginPass() {
        return userLoginPass;
    }

    public void setUserLoginPass(String userLoginPass) {
        this.userLoginPass = userLoginPass;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
