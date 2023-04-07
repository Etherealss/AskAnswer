package cn.hwb.askanswer.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分页方式
 * @author hwb
 */
@Getter
@AllArgsConstructor
public enum PagingType implements BaseEnum {
    CURSOR(0, "cursor"),
    OFFSET(1, "offset"),
    ;
    private final int code;
    private final String name;
}
