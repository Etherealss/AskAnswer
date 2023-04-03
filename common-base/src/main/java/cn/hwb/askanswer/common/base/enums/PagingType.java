package cn.hwb.askanswer.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wtk
 * @date 2023-03-25
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
