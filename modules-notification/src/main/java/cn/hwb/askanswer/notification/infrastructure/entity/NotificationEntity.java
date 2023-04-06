package cn.hwb.askanswer.notification.infrastructure.entity;

import cn.hwb.askanswer.common.base.pojo.entity.IdentifiedEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author wtk
 * @date 2023-04-06
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@TableName(value = "notification")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEntity extends IdentifiedEntity {
}
