package cn.hwb.askanswer.question.service.question;

import cn.hwb.askanswer.question.infrastructure.pojo.entity.QuestionEntity;
import cn.hwb.askanswer.question.mapper.QuestionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hwb
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService extends ServiceImpl<QuestionMapper, QuestionEntity> {

}
