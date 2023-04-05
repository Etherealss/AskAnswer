package cn.hwb.askanswer.collection.infrastructure.pojo.entity;

import cn.hwb.askanswer.common.base.pojo.entity.IdentifiedEntity;
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
@TableName(value = "like_count")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionCountEntity extends IdentifiedEntity {
    @TableField(value = "count")
    Integer count;
}
