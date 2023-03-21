package cn.hwb.askanswer.question.service.question;

import cn.hwb.common.base.pojo.entity.IdentifiedEntity;
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
public class Question extends IdentifiedEntity {
    @TableField(value = "title")
    String title;

    @TableField(value = "content")
    String content;

    @TableField(value = "tags")
    List<String> tags;

    @TableField(value = "age_bracket")
    Integer ageBracket;

    @TableField(value = "is_deleted")
    @TableLogic(value="0", delval="1")
    Boolean deleted;
}
