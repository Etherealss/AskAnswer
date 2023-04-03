package cn.hwb.askanswer.question.infrastructure.pojo.entity;

import cn.hwb.askanswer.common.database.repository.List2JsonTypeHandler;
import cn.hwb.askanswer.common.base.enums.AgeBracketEnum;
import cn.hwb.askanswer.common.base.pojo.entity.IdentifiedEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author wtk
 * @date 2023-03-21
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "question")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionEntity extends IdentifiedEntity {
    @TableField(value = "title")
    String title;

    @TableField(value = "content")
    String content;

    @TableField(value = "tags", typeHandler = List2JsonTypeHandler.class)
    List<String> tags;

    @TableField(value = "age_bracket")
    AgeBracketEnum ageBracket;

    @TableField(value = "is_deleted")
    @TableLogic(value="0", delval="1")
    Boolean isDeleted;
}
