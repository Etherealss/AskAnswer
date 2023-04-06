package cn.hwb.askanswer.collection.service;

import cn.hwb.askanswer.collection.infrastructure.pojo.entity.CollectionRelationEntity;
import cn.hwb.askanswer.collection.mapper.CollectionRelationMapper;
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
public class CollectionRelationService extends ServiceImpl<CollectionRelationMapper, CollectionRelationEntity> {

    @Transactional(rollbackFor = Exception.class)
    public void addCollection(Long userId, Long targetId) {
        CollectionRelationEntity entity = new CollectionRelationEntity();
        entity.setCreator(userId);
        entity.setTargetId(targetId);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean removeCollection(Long userId, Long targetId) {
        return this.lambdaUpdate()
                .eq(CollectionRelationEntity::getCreator, userId)
                .eq(CollectionRelationEntity::getTargetId, targetId)
                .remove();
    }

    public boolean isCollected(Long userId, Long targetId) {
        return this.lambdaQuery()
              .eq(CollectionRelationEntity::getCreator, userId)
              .eq(CollectionRelationEntity::getTargetId, targetId)
              .count() > 0;
    }

    public List<Long> page(Long userId, Long cursorId, int size) {
        List<Long> targetIds = this.lambdaQuery()
                .eq(CollectionRelationEntity::getCreator, userId)
                .gt(CollectionRelationEntity::getTargetId, cursorId)
                .orderByDesc(CollectionRelationEntity::getCreateTime)
                .last(String.format("LIMIT %d", size))
                .list()
                .stream()
                .map(CollectionRelationEntity::getTargetId)
                .collect(Collectors.toList());
        return targetIds;
    }
}
