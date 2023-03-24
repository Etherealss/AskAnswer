package cn.hwb.askanswer.question.mapper;

import cn.hwb.askanswer.question.infrastructure.pojo.entity.QuestionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Mapper
@Repository
public interface QuestionMapper extends BaseMapper<QuestionEntity> {
}
