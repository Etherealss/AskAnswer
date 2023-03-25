package cn.hwb.askanswer.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import cn.hwb.askanswer.comment.infrastructure.pojo.entity.CommentEntity;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<CommentEntity> {
}
