package cn.hwb.askanswer.answer.infrastructure.pojo.dto;

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
    Long creator;
    Date createTime;
    Date modifyTime;
    String title;
    String content;
}
