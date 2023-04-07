package cn.hwb.askanswer.collection.mapper;

import cn.hwb.askanswer.collection.infrastructure.pojo.entity.CollectionRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author hwb
 */
@Mapper
@Repository
public interface CollectionRelationMapper extends BaseMapper<CollectionRelationEntity> {
}