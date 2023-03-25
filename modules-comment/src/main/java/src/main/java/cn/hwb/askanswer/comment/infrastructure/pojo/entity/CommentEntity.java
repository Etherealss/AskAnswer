package src.main.java.cn.hwb.askanswer.comment.infrastructure.pojo.entity;

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
public class CommentEntity extends IdentifiedEntity {
    @TableField(value = "target_id")
    Long targetId;

    @TableField(value = "content")
    String content;

    @TableField(value = "is_deleted")
    @TableLogic(value="0", delval="1")
    Boolean isDeleted;
}
