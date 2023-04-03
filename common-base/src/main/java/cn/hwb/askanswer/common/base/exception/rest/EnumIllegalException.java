package cn.hwb.askanswer.common.base.exception.rest;


import cn.hwb.askanswer.common.base.enums.BaseEnum;

/**
 * @author wtk
 * @date 2022-04-24
 */
public class EnumIllegalException extends ParamErrorException {

    public EnumIllegalException(Class<? extends BaseEnum> clazz, Object param) {
        super("参数 '" + param.toString() + "'不匹配枚举类'" + clazz.getCanonicalName() + "'");
    }

    public EnumIllegalException(String message) {
        super(message);
    }
}
