package cn.hwb.askanswer.answer.infrastructure.pojo.dto;

import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
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
public class AnswerDTO {
    Long id;
    UserBriefDTO creator;
    Date createTime;
    Date modifyTime;
    String title;
    String content;
    Boolean isAnonymous;
}
