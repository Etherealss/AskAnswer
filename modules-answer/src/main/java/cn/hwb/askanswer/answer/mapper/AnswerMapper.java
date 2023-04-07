package cn.hwb.askanswer.answer.mapper;

import cn.hwb.askanswer.answer.infrastructure.pojo.entity.AnswerEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author hwb
 */
@Mapper
@Repository
public interface AnswerMapper extends BaseMapper<AnswerEntity> {
}
