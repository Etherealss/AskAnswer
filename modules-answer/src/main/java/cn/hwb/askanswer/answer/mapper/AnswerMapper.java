package cn.hwb.askanswer.answer.mapper;

import cn.hwb.askanswer.answer.infrastructure.pojo.entity.AnswerEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Mapper
@Repository
public interface AnswerMapper extends BaseMapper<AnswerEntity> {
}
