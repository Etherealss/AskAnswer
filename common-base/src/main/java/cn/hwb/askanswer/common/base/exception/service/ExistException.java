package cn.hwb.askanswer.common.base.exception.service;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;

/**
 * @author wtk
 * @description
 * @date 2021-08-12
 */
public class ExistException extends BadRequestException {
    public ExistException(Class<?> clazz) {
        super(ResultCode.EXIST, "对应的" + clazz.getSimpleName() + "已存在");
    }

    public ExistException(Class<?> clazz, String identification) {
        super(ResultCode.EXIST, "'" + identification + "'对应的" + clazz.getSimpleName() + "不存在");
    }
}
