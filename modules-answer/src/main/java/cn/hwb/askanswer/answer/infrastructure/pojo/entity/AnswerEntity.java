package cn.hwb.askanswer.answer.infrastructure.pojo.entity;

import cn.hwb.askanswer.common.base.pojo.entity.IdentifiedEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
@TableName(value = "answer")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerEntity extends IdentifiedEntity {
    @TableField(value = "question_id")
    Long questionId;

    @TableField(value = "title")
    String title;

    @TableField(value = "content")
    String content;

    /**
     * 是否采纳回答
     */
    @TableField(value = "is_accepted")
    Boolean isAccepted;

    @TableField(value = "is_deleted")
    @TableLogic(value="0", delval="1")
    Boolean isDeleted;
}
