package cn.hwb.askanswer.common.base.enums;

import cn.hwb.askanswer.common.base.exception.rest.EnumIllegalException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author wtk
 * @date 2022-04-21
 */
public interface BaseEnum {
    /**
     * 用于显示的枚举名
     * @return
     */
    String getName();

    /**
     * 存储到数据库的枚举值
     * @return
     */
    @JsonValue // 返回前端 code
    int getCode();

    /**
     * 按枚举的code获取枚举实例
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING) // 前端传来code字段，自动转为enum
    static <T extends BaseEnum> T fromCode(Class<T> enumType, int code) {
        for (T object : enumType.getEnumConstants()) {
            if (code == object.getCode()) {
                return object;
            }
        }
        throw new EnumIllegalException(enumType, code);
    }

    /**
     * 按枚举的name获取枚举实例
     */
    static <T extends BaseEnum> T fromName(Class<T> enumType, String name) {
        for (T object : enumType.getEnumConstants()) {
            if (name.equals(object.getName())) {
                return object;
            }
        }
        throw new EnumIllegalException(enumType, name);
    }
}
