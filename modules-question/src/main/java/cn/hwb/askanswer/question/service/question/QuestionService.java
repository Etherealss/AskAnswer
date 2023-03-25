package cn.hwb.askanswer.question.service.question;

import cn.hwb.askanswer.common.base.exception.service.NotCreatorException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.question.infrastructure.converter.QuestionConverter;
import cn.hwb.askanswer.question.infrastructure.pojo.dto.QuestionDTO;
import cn.hwb.askanswer.question.infrastructure.pojo.entity.QuestionEntity;
import cn.hwb.askanswer.question.infrastructure.pojo.request.CreateQuestionRequest;
import cn.hwb.askanswer.question.infrastructure.pojo.request.UpdateQuestionRequest;
import cn.hwb.askanswer.question.mapper.QuestionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService extends ServiceImpl<QuestionMapper, QuestionEntity> {
    public static final int PAGE_SIZE = 10;

    private final QuestionConverter converter;
    private final QuestionMapper questionMapper;

    public Long publish(CreateQuestionRequest req) {
        QuestionEntity questionEntity = converter.toEntity(req);
        this.save(questionEntity);
        return questionEntity.getId();
    }

    public void update(Long questionId, Long authorId, UpdateQuestionRequest req) {
        checkCreator(questionId, authorId);
        QuestionEntity updateEntity = converter.toEntity(req);
        this.lambdaUpdate()
                .eq(QuestionEntity::getId, questionId)
                .update(updateEntity);
    }

    public void deleteById(Long questionId, Long userId) {
        checkCreator(questionId, userId);
        this.lambdaUpdate()
                .eq(QuestionEntity::getId, questionId)
                .eq(QuestionEntity::getCreator, userId)
                .remove();
    }

    public QuestionDTO findById(Long questionId) {
        QuestionEntity entity = this.lambdaQuery()
                .eq(QuestionEntity::getId, questionId)
                .one();
        if (entity == null) {
            throw new NotFoundException(QuestionEntity.class, questionId.toString());
        }
        return converter.toDto(entity);
    }

    public void checkCreator(Long questionId, Long userId) {
        QuestionEntity entity = this.lambdaQuery()
                .eq(QuestionEntity::getId, questionId)
                .select(QuestionEntity::getCreator).one();
        if (entity == null) {
            throw new NotFoundException(QuestionEntity.class, questionId.toString());
        }
        if (!entity.getCreator().equals(userId)) {
            throw new NotCreatorException(userId, questionId);
        }
    }

    public PageDTO<QuestionDTO> page(Long cursorId, int size) {
        List<QuestionDTO> records = this.lambdaQuery()
                .gt(QuestionEntity::getId, cursorId)
                .orderByDesc(QuestionEntity::getId)
                .last(String.format("LIMIT %d", size))
                .list()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return PageDTO.<QuestionDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }
}
