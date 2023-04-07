package cn.hwb.askanswer.like.mapper;

import cn.hwb.askanswer.like.infrastructure.pojo.entity.LikeRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author hwb
 */
@Mapper
@Repository
public interface LikeRelationMapper extends BaseMapper<LikeRelationEntity> {
}