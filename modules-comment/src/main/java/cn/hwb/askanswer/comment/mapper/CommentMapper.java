package cn.hwb.askanswer.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import cn.hwb.askanswer.comment.infrastructure.pojo.entity.CommentEntity;

/**
 * @author hwb
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<CommentEntity> {
}
