package cn.hwb.askanswer.like.mapper;

import cn.hwb.askanswer.like.infrastructure.pojo.entity.LikeCountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author wtk
 * @date 2023-04-05
 */
@Mapper
@Repository
public interface LikeCountMapper extends BaseMapper<LikeCountEntity> {

    void incre(@Param("targetId") Long targetId, @Param("increment") Integer increment);
}