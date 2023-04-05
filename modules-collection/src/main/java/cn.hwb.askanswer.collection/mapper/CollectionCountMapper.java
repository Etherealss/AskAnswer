package cn.hwb.askanswer.collection.mapper;

import cn.hwb.askanswer.collection.infrastructure.pojo.entity.CollectionCountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author wtk
 * @date 2023-04-05
 */
@Mapper
@Repository
public interface CollectionCountMapper extends BaseMapper<CollectionCountEntity> {

}