package cn.hwb.askanswer.collection.service;

import cn.hwb.askanswer.common.base.exception.service.ExistException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void like(Long userId, Long targetId) {
        if (collectionRelationService.isLiked(userId, targetId)) {
            throw new ExistException("点赞记录已存在");
        }
        collectionRelationService.create(userId, targetId);
        collectionCountService.add(targetId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void dislike(Long userId, Long targetId) {
        if (!collectionRelationService.isLiked(userId, targetId)) {
            throw new NotFoundException("点赞记录不存在");
        }
        collectionRelationService.remove(userId, targetId);
        collectionCountService.sub(targetId);
    }
}
