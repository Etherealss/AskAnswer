package cn.hwb.askanswer.common.base.config;

import org.springframework.http.HttpStatus;

/**
 * @author wtk
 * @date 2023-03-20
 */
public enum ResultCode {
    ;
    final HttpStatus httpStatus;
    final int code;
    final String message;

    ResultCode(int code, String message) {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.message = message;
    }

    ResultCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
