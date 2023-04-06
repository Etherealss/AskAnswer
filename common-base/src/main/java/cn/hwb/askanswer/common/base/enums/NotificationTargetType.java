package cn.hwb.askanswer.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Getter
@AllArgsConstructor
public enum NotificationTargetType implements BaseEnum {
    COMMENT(1, "comment"),
    ANSWER(2, "answer"),
    QUESTION(3, "question"),
    ;

    private final int code;
    private final String name;

}
