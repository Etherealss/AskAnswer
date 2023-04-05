package cn.hwb.askanswer.collection.service;

import cn.hwb.askanswer.collection.infrastructure.pojo.entity.CollectionRelationEntity;
import cn.hwb.askanswer.collection.mapper.CollectionRelationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class CollectionRelationService extends ServiceImpl<CollectionRelationMapper, CollectionRelationEntity> {

    @Transactional(rollbackFor = Exception.class)
    public void create(Long userId, Long targetId) {
        CollectionRelationEntity entity = new CollectionRelationEntity();
        entity.setCreator(userId);
        entity.setTargetId(targetId);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Long userId, Long targetId) {
        return this.lambdaUpdate()
                .eq(CollectionRelationEntity::getCreator, userId)
                .eq(CollectionRelationEntity::getTargetId, targetId)
                .remove();
    }

    public boolean isLiked(Long userId, Long targetId) {
        return this.lambdaQuery()
              .eq(CollectionRelationEntity::getCreator, userId)
              .eq(CollectionRelationEntity::getTargetId, targetId)
              .count() > 0;
    }
}
