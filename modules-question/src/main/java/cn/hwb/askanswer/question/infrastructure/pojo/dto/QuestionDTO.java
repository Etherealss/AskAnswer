package cn.hwb.askanswer.question.infrastructure.pojo.dto;

import cn.hwb.askanswer.common.base.enums.AgeBracketEnum;
import cn.hwb.askanswer.user.infrastructure.pojo.dto.UserBriefDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

/**
 * @author wtk
 * @date 2023-03-24
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionDTO {
    Long id;
    UserBriefDTO creator;
    Date createTime;
    Date modifyTime;
    String title;
    String content;
    List<String> tags;
    AgeBracketEnum ageBracket;
}
