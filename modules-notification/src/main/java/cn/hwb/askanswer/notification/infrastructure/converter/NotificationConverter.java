package cn.hwb.askanswer.notification.infrastructure.converter;

import cn.hwb.askanswer.common.base.enums.MapperComponentModel;
import cn.hwb.askanswer.notification.infrastructure.pojo.entity.NotificationEntity;
import cn.hwb.askanswer.notification.infrastructure.pojo.request.PublishNotificationRequest;
import cn.hwb.askanswer.notification.infrastructure.pojo.resp.NotificationDTO;
import org.mapstruct.Mapper;

/**
 * @author hwb
 */
@Mapper(componentModel = MapperComponentModel.SPRING)
public interface NotificationConverter {
    NotificationEntity toEntity(PublishNotificationRequest command);
    NotificationDTO toDTO(NotificationEntity entity);
}
