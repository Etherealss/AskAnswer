package cn.hwb.askanswer.like.service;

import cn.hwb.askanswer.common.base.exception.service.ExistException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.like.infrastructure.enums.LikeTargetType;
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
public class LikeService {
    private final LikeCountService likeCountService;
    private final LikeRelationService likeRelationService;

    /**
     * 点赞
     * @param userId
     * @param targetId
     * @param targetType
     */
    @Transactional(rollbackFor = Exception.class)
    public void like(Long userId, Long targetId, LikeTargetType targetType) {
        if (likeRelationService.isLiked(userId, targetId)) {
            throw new ExistException("点赞记录已存在");
        }
        // 创建点赞关系
        likeRelationService.create(userId, targetId, targetType);
        // 点赞数+1
        likeCountService.increase(targetId);
    }

    /**
     * 取消点赞
     * @param userId
     * @param targetId
     */
    @Transactional(rollbackFor = Exception.class)
    public void dislike(Long userId, Long targetId) {
        if (!likeRelationService.isLiked(userId, targetId)) {
            throw new NotFoundException("点赞记录不存在");
        }
        // 移除点赞关系
        likeRelationService.remove(userId, targetId);
        // 点赞数-1
        likeCountService.decrease(targetId);
    }
}
