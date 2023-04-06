package cn.hwb.askanswer.like.infrastructure.enums;

import cn.hwb.askanswer.common.base.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@AllArgsConstructor
@Getter
public enum LikeTargetType implements BaseEnum {
    ANSWER(0, "answer"),
    COMMENT(1, "comment"),
    ;

    private final int code;
    private final String name;
}
