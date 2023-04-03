package cn.hwb.askanswer.common.base.exception.rest;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;

/**
 * @author wtk
 * @description
 * @date 2021-08-13
 */
public class ParamMissingException extends BadRequestException {
    public ParamMissingException() {
        super(ResultCode.MISSING_PARAM);
    }

    public ParamMissingException(String paramName) {
        super(ResultCode.MISSING_PARAM, paramName + "参数缺失");
    }

    public ParamMissingException(String paramName, String desc) {
        super(ResultCode.MISSING_PARAM, paramName + "参数缺失，" + desc);
    }
}
