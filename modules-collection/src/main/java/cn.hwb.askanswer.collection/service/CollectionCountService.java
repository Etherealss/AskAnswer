package cn.hwb.askanswer.collection.service;

import cn.hwb.askanswer.collection.infrastructure.pojo.entity.CollectionCountEntity;
import cn.hwb.askanswer.collection.mapper.CollectionCountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * @author hwb
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionCountService extends ServiceImpl<CollectionCountMapper, CollectionCountEntity> {
    private static final int DEAFULT_COUNT = 0;

    private final CollectionCountMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public void create(Long targetId) {
        mapper.insert(new CollectionCountEntity(targetId, DEAFULT_COUNT));
    }

    @Transactional(rollbackFor = Exception.class)
    public void increase(Long targetId) {
        mapper.incre(targetId, 1);
    }

    @Transactional(rollbackFor = Exception.class)
    public void decrease(Long targetId) {
        mapper.incre(targetId, -1);
    }

    public int get(Long targetId) {
        Optional<CollectionCountEntity> opt = this.lambdaQuery()
                .eq(CollectionCountEntity::getId, targetId)
                .oneOpt();
        return opt.isPresent() ? opt.get().getCount() : 0;
    }

    public List<CollectionCountEntity> pageOrderByCount(Long cursorId, Integer cursorCount, int size) {
        return this.lambdaQuery()
                .le(CollectionCountEntity::getCount, cursorCount)
                .le(CollectionCountEntity::getId, cursorId)
                // id越大说明发布时间越新
                .orderByDesc(CollectionCountEntity::getCount, CollectionCountEntity::getId)
                .select(CollectionCountEntity::getId, CollectionCountEntity::getCount)
                .last(String.format("LIMIT %d", size))
                .list();
    }
}
