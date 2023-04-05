package cn.hwb.askanswer.like.service;

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
public class LikeService {
    private final LikeCountService likeCountService;
    private final LikeRelationService likeRelationService;

    @Transactional(rollbackFor = Exception.class)
    public void like(Long userId, Long targetId) {
        if (likeRelationService.isLiked(userId, targetId)) {
            throw new ExistException("点赞记录已存在");
        }
        likeRelationService.create(userId, targetId);
        likeCountService.add(targetId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void dislike(Long userId, Long targetId) {
        if (!likeRelationService.isLiked(userId, targetId)) {
            throw new NotFoundException("点赞记录不存在");
        }
        likeRelationService.remove(userId, targetId);
        likeCountService.sub(targetId);
    }
}
