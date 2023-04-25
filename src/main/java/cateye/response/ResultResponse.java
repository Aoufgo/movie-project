package cateye.response;

public class ResultResponse {
    /**
     * 通用返回成功（没有返回结果）
     * @param
     * @return
     */
    public static HttpResult success(){
        return new HttpResult();
    }

    /**
     * 返回成功（有返回结果）
     * @param data
     * @return
     */
    public static<T> HttpResult<T> success(T data){
        return new HttpResult<T>(data);
    }

    /**
     * 通用返回失败
     * @param resultCode
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> failure(ResultCodeEnum resultCode){
        return new HttpResult<T>(resultCode);
    }

    /**
     * 捕获异常类型设置状态和提示信息
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> failure(Integer code, String message) {
        ResultCodeEnum resultCodeEnum = ResultCodeEnum.BUSINESS_EXCEPTION;
        resultCodeEnum.setCode(code);
        resultCodeEnum.setMessage(message);
        return failure(resultCodeEnum);
    }
}
