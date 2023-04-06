package cn.hwb.askanswer.like.service;

import cn.hwb.askanswer.like.infrastructure.pojo.entity.LikeRelationEntity;
import cn.hwb.askanswer.like.mapper.LikeRelationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wtk
 * @date 2023-04-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LikeRelationService extends ServiceImpl<LikeRelationMapper, LikeRelationEntity> {

    @Transactional(rollbackFor = Exception.class)
    public void create(Long userId, Long targetId) {
        LikeRelationEntity entity = new LikeRelationEntity();
        entity.setCreator(userId);
        entity.setTargetId(targetId);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Long userId, Long targetId) {
        return this.lambdaUpdate()
                .eq(LikeRelationEntity::getCreator, userId)
                .eq(LikeRelationEntity::getTargetId, targetId)
                .remove();
    }

    public boolean isLiked(Long userId, Long targetId) {
        return this.lambdaQuery()
              .eq(LikeRelationEntity::getCreator, userId)
              .eq(LikeRelationEntity::getTargetId, targetId)
              .count() > 0;
    }

    public List<Long> page(Long cursorId, int size) {
        List<Long> targetIds = this.lambdaQuery()
                .gt(LikeRelationEntity::getTargetId, cursorId)
                .orderByDesc(LikeRelationEntity::getCreateTime)
                .last(String.format("LIMIT %d", size))
                .list()
                .stream()
                .map(LikeRelationEntity::getTargetId)
                .collect(Collectors.toList());
        return targetIds;
    }
}
