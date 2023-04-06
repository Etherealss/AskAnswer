package cn.hwb.askanswer.collection.service;

import cn.hwb.askanswer.common.base.exception.service.ExistException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wtk
 * @date 2023-04-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionService {
    private final CollectionCountService collectionCountService;
    private final CollectionRelationService collectionRelationService;

    @Transactional(rollbackFor = Exception.class)
    public void addCollection(Long userId, Long targetId) {
        if (collectionRelationService.isCollected(userId, targetId)) {
            throw new ExistException("点赞记录已存在");
        }
        collectionRelationService.addCollection(userId, targetId);
        collectionCountService.increase(targetId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeCollection(Long userId, Long targetId) {
        if (!collectionRelationService.isCollected(userId, targetId)) {
            throw new NotFoundException("点赞记录不存在");
        }
        collectionRelationService.removeCollection(userId, targetId);
        collectionCountService.decrease(targetId);
    }
}
