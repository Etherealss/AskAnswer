package cn.hwb.askanswer.collection.mapper;

import cn.hwb.askanswer.collection.infrastructure.pojo.entity.CollectionCountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author hwb
 */
@Mapper
@Repository
public interface CollectionCountMapper extends BaseMapper<CollectionCountEntity> {
    void incre(@Param("targetId") Long targetId, @Param("increment") Integer increment);
}