package cn.hwb.askanswer.common.base.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * @author wang tengkun
 * @date 2022/2/23
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class IdentifiedEntity extends BaseEntity {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected Long id;
}
