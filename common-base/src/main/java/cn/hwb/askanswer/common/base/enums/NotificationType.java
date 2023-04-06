package cn.hwb.askanswer.common.base.enums;

import cn.hwb.askanswer.common.base.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Getter
@AllArgsConstructor
public enum NotificationType implements BaseEnum {
    LIKE(0, "like"),
    COMMENT(1, "comment"),
    ANSWER(2, "answer"),
    ;

    private final int code;
    private final String name;

}