package cn.hwb.askanswer.like.infrastructure.pojo.entity;

import cn.hwb.askanswer.common.base.pojo.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author wtk
 * @date 2023-04-05
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "like_relation")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikeRelationEntity extends BaseEntity {
    @TableField(value = "target_id")
    Long targetId;
}
