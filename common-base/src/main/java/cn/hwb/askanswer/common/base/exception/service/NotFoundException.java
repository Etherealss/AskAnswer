package cn.hwb.askanswer.common.base.exception.service;


import cn.hwb.askanswer.common.base.enums.ResultCode;
import cn.hwb.askanswer.common.base.exception.BadRequestException;
import lombok.Getter;

/**
 * @author wtk
 * @description
 * @date 2021-08-12
 */
@Getter
public class NotFoundException extends BadRequestException {

    private final String identification;

    public NotFoundException(String msg) {
        super(ResultCode.NOT_FOUND, msg);
        identification = null;
    }

    public NotFoundException(Class<?> clazz, String identification) {
        super(ResultCode.NOT_FOUND, "'" + identification + "'对应的" + clazz.getSimpleName() + "不存在");
        this.identification = identification;
    }
}
