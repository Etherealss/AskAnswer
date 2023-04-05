package cn.hwb.askanswer.collection.service;

import cn.hwb.askanswer.collection.infrastructure.pojo.entity.CollectionCountEntity;
import cn.hwb.askanswer.collection.mapper.CollectionCountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * @author wtk
 * @date 2023-04-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionCountService extends ServiceImpl<CollectionCountMapper, CollectionCountEntity> {

    @Transactional(rollbackFor = Exception.class)
    public void add(Long targetId) {
        this.incre(targetId, 1);
    }

    @Transactional(rollbackFor = Exception.class)
    public void sub(Long targetId) {
        this.incre(targetId, -1);
    }

    private void incre(Long targetId, int value) {
        Optional<CollectionCountEntity> opt = this.lambdaQuery()
                .eq(CollectionCountEntity::getId, targetId)
                .select(CollectionCountEntity::getId, CollectionCountEntity::getCount)
                .oneOpt();
        if (opt.isPresent()) {
            CollectionCountEntity entity = opt.get();
            entity.setCount(entity.getCount() + value);
            this.updateById(entity);
        } else {
            CollectionCountEntity entity = new CollectionCountEntity();
            entity.setId(targetId);
            entity.setCount(value);
            this.save(entity);
        }
    }

    public int get(Long targetId) {
        Optional<CollectionCountEntity> opt = this.lambdaQuery()
                .eq(CollectionCountEntity::getId, targetId)
                .oneOpt();
        return opt.isPresent() ? opt.get().getCount() : 0;
    }
}
