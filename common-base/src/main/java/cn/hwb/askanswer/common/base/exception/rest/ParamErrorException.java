package cn.hwb.askanswer.common.base.exception.rest;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;

/**
 * 参数错误
 * @author hwb
 * @description
 
 */
public class ParamErrorException extends BadRequestException {
    public ParamErrorException() {
        super(ResultCode.ERROR_PARAM);
    }

    public ParamErrorException(String message) {
        super(ResultCode.ERROR_PARAM, message);
    }
}
