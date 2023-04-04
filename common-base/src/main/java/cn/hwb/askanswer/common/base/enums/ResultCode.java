package cn.hwb.askanswer.common.base.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author wtk
 * @date 2023-03-20
 */
@Getter
public enum ResultCode {
    // --------------- common 400 ---------------
    OK(HttpStatus.OK, 20000001, "OK"),
    BAD_REQUEST(40000001, "请求报文语法错误[参数校验失败]"),
    MISSING_PARAM(40000002, "[参数缺失]"),
    ERROR_PARAM(40000003, "[参数不合法]"),
    EXIST(40000004, "[目标已存在]"),
    MISMATCH(40000005, "[信息不匹配]"),
    OPERATE_UNSUPPORTED(40000006, "[请求不支持]"),
    NOT_AUTHOR(40000007, "[非创建者]"),
    // --------------- common 401 ---------------
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 40100001, "[未认证身份]"),
    // --------------- common 403 ---------------
    PERMISSION_NOT_MATCH(HttpStatus.FORBIDDEN, 40300001, "[没有访问权限]"),
    ROLE_NOT_MATCH(HttpStatus.FORBIDDEN, 40300002, "[非可访问角色]"),
    // --------------- common 404 ---------------
    NOT_FOUND(HttpStatus.NOT_FOUND, 40400001, "[目标不存在]"),
    // --------------- common 500 ---------------
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000001, "[服务运行异常]"),

    // --------------- user 01 ---------------
    USER_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, 40101001, "[用户token缺失]"),
    USER_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, 40101002, "[用户token无效或已过期]"),
    PASSWORD_NOT_MATCH(40001003, "[密码错误]"),
    USER_UN_REVIEW(HttpStatus.FORBIDDEN, 40301004, "[用户审核未完成]"),

    // --------------- question 02 ---------------

    // --------------- answer 03 ---------------
    ANSWER_TARGET_NOT_MATCH(40003001, "[回答目标不匹配]"),

    // --------------- comment 04 ---------------
    COMMENT_TARGET_NOT_MATCH(40004001, "[评论目标不匹配]"),

    // --------------- like ---------------
    // --------------- notification ---------------
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
