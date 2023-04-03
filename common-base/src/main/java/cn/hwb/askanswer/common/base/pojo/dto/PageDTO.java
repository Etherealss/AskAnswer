package cn.hwb.askanswer.common.base.pojo.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author wtk
 * @date 2022-08-14
 */
@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class PageDTO<T> {

    /**
     * 查询数据列表
     */
    List<T> records;

    /**
     * 当前页
     */
    Integer currentPage;

    /**
     * 总页数
     */
    Integer totalPage;

    /**
     * 每页显示条数
     */
    Integer pageSize;

    /**
     * 总记录数
     */
    Integer totalSize;
}
