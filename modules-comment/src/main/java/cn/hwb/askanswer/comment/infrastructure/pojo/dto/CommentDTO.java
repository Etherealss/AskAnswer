package cn.hwb.askanswer.comment.infrastructure.pojo.dto;

import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author hwb
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTO {
    Long id;
    UserBriefDTO creator;
    Date createTime;
    String content;
    Long targetId;
    Long containerId;
}
