package cn.hwb.askanswer.notification.service;

import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.notification.infrastructure.converter.NotificationConverter;
import cn.hwb.askanswer.notification.infrastructure.pojo.entity.NotificationEntity;
import cn.hwb.askanswer.notification.infrastructure.pojo.request.PublishNotificationRequest;
import cn.hwb.askanswer.notification.infrastructure.pojo.resp.NotificationResp;
import cn.hwb.askanswer.notification.mapper.NotificationMapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wtk
 * @date 2023-04-06
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService extends ServiceImpl<NotificationMapper, NotificationEntity> {

    private final NotificationConverter notificationConverter;

    /**
     * 游标分页获取通知
     * @param rcvrId 通知的接收者的ID
     * @param type 通知类型
     * @param cursorId 游标ID
     * @param size 分页显示的数量
     * @return
     */
    public PageDTO<NotificationResp> page(Long rcvrId, String type, Long cursorId, int size) {
        LambdaQueryChainWrapper<NotificationEntity> query = this.lambdaQuery()
                .eq(NotificationEntity::getRcvrId, rcvrId);
        if (type != null) {
            // 按类型查
            query.eq(NotificationEntity::getType, type);
        }
        List<NotificationResp> records = query
                .last(String.format("LIMIT %d", size))
                .list()
                .stream()
                .map(notificationConverter::toResp)
                .collect(Collectors.toList());
        return PageDTO.<NotificationResp>builder()
                .records(records)
                .pageSize(size)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void publish(PublishNotificationRequest command) {
        NotificationEntity entity = notificationConverter.toEntity(command);
        entity.setIsRead(false);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }
}
