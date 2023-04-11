package cn.hwb.askanswer.common.base.exception.service;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;

/**
 * 目标已存在
 * @author hwb
 */
public class ExistException extends BadRequestException {
    public ExistException(Class<?> clazz) {
        super(ResultCode.EXIST, "对应的" + clazz.getSimpleName() + "已存在");
    }

    public ExistException(String message) {
        super(ResultCode.EXIST, message);
    }

    public ExistException(Class<?> clazz, String identification) {
        super(ResultCode.EXIST, "'" + identification + "'对应的" + clazz.getSimpleName() + "已经存在");
    }
}
