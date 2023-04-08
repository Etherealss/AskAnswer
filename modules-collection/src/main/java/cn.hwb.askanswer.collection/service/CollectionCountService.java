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
 * @author hwb
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionCountService extends ServiceImpl<CollectionCountMapper, CollectionCountEntity> {
    private final CollectionCountMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public void increase(Long targetId) {
        // 如果记录不存在，则创建，设置count为1；如果记录已存在，则更新count自增1
        // 使用 ON DUPLICATE KEY UPDATE 实现"创建或更新"操作
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
}
