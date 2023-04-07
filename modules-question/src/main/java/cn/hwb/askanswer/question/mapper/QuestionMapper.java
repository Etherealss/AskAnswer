package cn.hwb.askanswer.question.mapper;

import cn.hwb.askanswer.question.infrastructure.pojo.entity.QuestionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author hwb
 */
@Mapper
@Repository
public interface QuestionMapper extends BaseMapper<QuestionEntity> {
}
