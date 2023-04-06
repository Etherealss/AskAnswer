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
 * @author wtk
 * @date 2023-04-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LikeCountService extends ServiceImpl<LikeCountMapper, LikeCountEntity> {

    @Transactional(rollbackFor = Exception.class)
    public void increase(Long targetId) {
        this.incre(targetId, 1);
    }

    @Transactional(rollbackFor = Exception.class)
    public void decrease(Long targetId) {
        this.incre(targetId, -1);
    }

    private void incre(Long targetId, int value) {
        Optional<LikeCountEntity> opt = this.lambdaQuery()
                .eq(LikeCountEntity::getId, targetId)
                .select(LikeCountEntity::getId, LikeCountEntity::getCount)
                .oneOpt();
        if (opt.isPresent()) {
            LikeCountEntity entity = opt.get();
            entity.setCount(entity.getCount() + value);
            this.updateById(entity);
        } else {
            LikeCountEntity entity = new LikeCountEntity();
            entity.setId(targetId);
            entity.setCount(value);
            this.save(entity);
        }
    }

    public int get(Long targetId) {
        Optional<LikeCountEntity> opt = this.lambdaQuery()
                .eq(LikeCountEntity::getId, targetId)
                .oneOpt();
        return opt.isPresent() ? opt.get().getCount() : 0;
    }
}
