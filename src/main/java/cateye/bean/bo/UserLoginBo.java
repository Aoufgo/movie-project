package cateye.bean.bo;

import javax.validation.constraints.NotBlank;

public class UserLoginBo {
    @NotBlank(message = "用户名或手机号不能为空!")
    private String userLoginName;
    @NotBlank(message = "密码不能为空!")
    private String userLoginPass;

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
}
