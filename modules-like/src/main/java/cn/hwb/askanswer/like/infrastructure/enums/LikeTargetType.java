package cn.hwb.askanswer.like.infrastructure.enums;

import cn.hwb.askanswer.common.base.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hwb
 */
@AllArgsConstructor
@Getter
public enum LikeTargetType implements BaseEnum {
    COMMENT(1, "comment"),
    ANSWER(2, "answer"),
    ;

    private final int code;
    private final String name;
}
