package cn.hwb.askanswer.notification.infrastructure.converter;

import cn.hwb.askanswer.common.base.enums.MapperComponentModel;
import cn.hwb.askanswer.notification.infrastructure.pojo.entity.NotificationEntity;
import cn.hwb.askanswer.notification.infrastructure.pojo.request.PublishNotificationRequest;
import cn.hwb.askanswer.notification.infrastructure.pojo.resp.NotificationResp;
import org.mapstruct.Mapper;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Mapper(componentModel = MapperComponentModel.SPRING)
public interface NotificationConverter {
    NotificationEntity toEntity(PublishNotificationRequest command);
    NotificationResp toResp(NotificationEntity entity);
}
