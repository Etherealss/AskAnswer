package cn.hwb.askanswer.comment.infrastructure.pojo.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author wtk
 * @date 2023-03-24
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTO {
    Long id;
    Long creator;
    Date createTime;
    String content;
    Long targetId;
    Long containerId;
}
