package cn.hwb.askanswer.common.base.exception;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author wtk
 * @description 自定义 服务异常
 * @date 2021-08-12
 */
@Setter
@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {

    private int code;
    private HttpStatus httpStatus;

    public BaseException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.httpStatus = resultCode.getHttpStatus();
    }

    public BaseException(ResultCode resultCode, String message) {
        super(resultCode.getMessage() + message);
        this.code = resultCode.getCode();
        this.httpStatus = resultCode.getHttpStatus();
    }

    /**
     * @param resultCode
     * @param message
     * @param e 原始异常
     */
    public BaseException(ResultCode resultCode, String message, Throwable e) {
        super(resultCode.getMessage() + message, e);
        this.code = resultCode.getCode();
        this.httpStatus = resultCode.getHttpStatus();
    }
}
