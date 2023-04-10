package cn.hwb.askanswer.collection.infrastructure.pojo.entity;

import cn.hwb.askanswer.common.base.pojo.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName(value = "collection_count")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionCountEntity extends BaseEntity {
    /**
     * 手动输入主键值
     */
    @TableId(value = "id", type = IdType.INPUT)
    protected Long id;

    @TableField(value = "count")
    Integer count;
}
