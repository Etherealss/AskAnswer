package src.main.java.cn.hwb.askanswer.answer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import src.main.java.cn.hwb.askanswer.answer.infrastructure.pojo.entity.CommentEntity;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<CommentEntity> {
}
