package cn.hwb.askanswer.like.infrastructure.pojo.entity;

import cn.hwb.askanswer.common.base.pojo.entity.IdentifiedEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author hwb
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "like_count")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikeCountEntity extends IdentifiedEntity {
    @TableField(value = "count")
    Integer count;
}
