package cateye.response;

import java.io.Serializable;

public class HttpResult<T> implements Serializable {

    private Boolean success;

    private Integer code;

    private String message;

    private T data;

    /**
     * 无参构造
     */
    public HttpResult() {
        this.success = true;
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.message = ResultCodeEnum.SUCCESS.getMessage();
    }

    /**
     * 有参构造：返回成功
     * @param object
     */
    public HttpResult(T object) {
        this.success = true;
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.message = ResultCodeEnum.SUCCESS.getMessage();
        this.data = object;
    }

    /**
     * 有参构造：返回失败
     * @param resultCode
     */
    public HttpResult(ResultCodeEnum resultCode) {
        this.success = false;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
