package cn.hwb.askanswer.question.infrastructure.enums;

import cn.hwb.askanswer.common.base.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Getter
@AllArgsConstructor
public enum AgeBracketEnum implements BaseEnum {
    CHILDHOOD(1, "童年"),
    ADOLESCENCE(2, "少年"),
    ADULTHOOD(3, "青年"),
    MIDDLE(4, "中年"),
    LATE(5, "中老年"),
    OLDER(6, "老年"),
    ;
    final int code;
    final String name;
}
