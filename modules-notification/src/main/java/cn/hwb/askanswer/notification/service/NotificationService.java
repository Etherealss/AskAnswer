package cn.hwb.askanswer.notification.service;

import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.notification.infrastructure.converter.NotificationConverter;
import cn.hwb.askanswer.notification.infrastructure.pojo.entity.NotificationEntity;
import cn.hwb.askanswer.notification.infrastructure.pojo.request.PublishNotificationCommand;
import cn.hwb.askanswer.notification.infrastructure.pojo.resp.NotificationResp;
import cn.hwb.askanswer.notification.mapper.NotificationMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    public PageDTO<NotificationResp> page(Long userId, int currentPage, int size, boolean unread) {
        Page<NotificationEntity> page = this.lambdaQuery()
                .eq(NotificationEntity::getRcvrId, userId)
                .page(new Page<>(currentPage, size));
        List<NotificationResp> records = page.getRecords().stream()
                .map(notificationConverter::toResp)
                .collect(Collectors.toList());
        return new PageDTO<>(records, page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void publish(PublishNotificationCommand command) {
        NotificationEntity entity = notificationConverter.toEntity(command);
        entity.setIsRead(false);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }
}
