package cn.hwb.askanswer.notification.infrastructure.pojo.entity;

import cn.hwb.askanswer.common.base.pojo.entity.IdentifiedEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
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
// 需要加上 autoResultMap = true 才能使用 JacksonTypeHandler 获取数据，否则获取到的为null
@TableName(value = "notification", autoResultMap = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEntity extends IdentifiedEntity {
    @TableField("type")
    String type;

    @TableField(value = "props", typeHandler = JacksonTypeHandler.class)
    Object props;

    @TableField("rcvr_id")
    Long rcvrId;

    @TableField("is_read")
    Boolean isRead;
}
