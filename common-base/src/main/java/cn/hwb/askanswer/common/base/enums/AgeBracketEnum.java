package cn.hwb.askanswer.common.base.enums;

import cn.hutool.core.date.DateUtil;
import cn.hwb.askanswer.common.base.exception.rest.EnumIllegalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * 年龄段枚举
 * @author hwb
 */
@Getter
@AllArgsConstructor
public enum AgeBracketEnum implements BaseEnum {
    CHILDHOOD(1, "少年", "少年", 0),
    ADOLESCENCE(2, "花季", "青少年", 15),
    ADULTHOOD(3, "青春", "青年", 18),
    YOUNG(4, "入世", "壮年", 25),
    MIDDLE_AGE(5, "发展", "壮年", 28),
    ELDERLY(6, "沉淀", "中年", 35),
    OLDER(7, "淡薄", "老年", 65),
    ;
    final int code;
    /**
     * 展示名
     */
    final String name;
    /**
     * 描述信息
     */
    final String desc;
    /**
     * 最小年龄
     */
    final int minAge;

    public static AgeBracketEnum getByBirthday(Date birthday) {
        int age = DateUtil.age(birthday, new Date());
        for (AgeBracketEnum value : AgeBracketEnum.values()) {
            if (value.getMinAge() >= age) {
                return value;
            }
        }
        throw new EnumIllegalException("没有该年龄匹配的年龄段：" + age);
    }
}
