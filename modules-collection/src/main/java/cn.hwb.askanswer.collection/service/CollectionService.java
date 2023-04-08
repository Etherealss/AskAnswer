package cn.hwb.askanswer.collection.service;

import cn.hwb.askanswer.common.base.exception.service.ExistException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hwb
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionService {
    private final CollectionCountService collectionCountService;
    private final CollectionRelationService collectionRelationService;

    /**
     * 收藏
     * @param userId
     * @param targetId
     */
    @Transactional(rollbackFor = Exception.class)
    public void collect(Long userId, Long targetId) {
        if (collectionRelationService.isCollected(userId, targetId)) {
            throw new ExistException("收藏记录已存在");
        }
        // 创建收藏关系
        collectionRelationService.create(userId, targetId);
        // 收藏数+1
        collectionCountService.increase(targetId);
    }

    /**
     * 取消收藏
     * @param userId
     * @param targetId
     */
    @Transactional(rollbackFor = Exception.class)
    public void uncollect(Long userId, Long targetId) {
        if (!collectionRelationService.isCollected(userId, targetId)) {
            throw new NotFoundException("收藏记录不存在");
        }
        // 删除收藏关系
        collectionRelationService.removeCollection(userId, targetId);
        // 取消收藏数-1
        collectionCountService.decrease(targetId);
    }
}
