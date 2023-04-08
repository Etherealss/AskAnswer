package cn.hwb.askanswer.like.service;

import cn.hwb.askanswer.like.infrastructure.pojo.entity.LikeCountEntity;
import cn.hwb.askanswer.like.mapper.LikeCountMapper;
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
public class LikeCountService extends ServiceImpl<LikeCountMapper, LikeCountEntity> {
    private final LikeCountMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public void increase(Long targetId) {
        // 如果记录不存在，则创建，设置count为1；如果记录已存在，则更新count自增1
        // 使用 ON DUPLICATE KEY UPDATE 实现"创建或更新"操作
        mapper.incre(targetId, 1);
    }

    @Transactional(rollbackFor = Exception.class)
    public void decrease(Long targetId) {
        // count = count-1
        mapper.incre(targetId, -1);
    }

    public int get(Long targetId) {
        Optional<LikeCountEntity> opt = this.lambdaQuery()
                .eq(LikeCountEntity::getId, targetId)
                .oneOpt();
        return opt.isPresent() ? opt.get().getCount() : 0;
    }
}
