package cn.hwb.askanswer.common.base.exception.rest;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;

/**
 * @author wtk
 * @description
 * @date 2021-08-13
 */
public class ParamErrorException extends BadRequestException {
    public ParamErrorException() {
        super(ResultCode.ERROR_PARAM);
    }

    public ParamErrorException(String message) {
        super(ResultCode.ERROR_PARAM, message);
    }
}
