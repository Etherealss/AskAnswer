package cn.hwb.askanswer.answer.service.answer;

import cn.hwb.askanswer.answer.infrastructure.converter.AnswerConverter;
import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.infrastructure.pojo.entity.AnswerEntity;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.CreateAnswerRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerRequest;
import cn.hwb.askanswer.answer.mapper.AnswerMapper;
import cn.hwb.askanswer.common.base.exception.service.NotCreatorException;
import cn.hwb.askanswer.common.base.exception.service.NotFoundException;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
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
public class AnswerService extends ServiceImpl<AnswerMapper, AnswerEntity> {
    public static final int PAGE_SIZE = 10;

    private final AnswerConverter converter;
    private final AnswerMapper questionMapper;

    public Long publish(Long questionId, CreateAnswerRequest req) {
        AnswerEntity answerEntity = converter.toEntity(req);
        answerEntity.setQuestionId(questionId);
        this.save(answerEntity);
        return answerEntity.getId();
    }

    public void update(Long questionId, Long authorId, UpdateAnswerRequest req) {
        checkCreator(questionId, authorId);
        AnswerEntity updateEntity = converter.toEntity(req);
        this.lambdaUpdate()
                .eq(AnswerEntity::getId, questionId)
                .update(updateEntity);
    }

    public void deleteById(Long questionId, Long userId) {
        checkCreator(questionId, userId);
        this.lambdaUpdate()
                .eq(AnswerEntity::getId, questionId)
                .eq(AnswerEntity::getCreator, userId)
                .remove();
    }

    public AnswerDTO findById(Long questionId) {
        AnswerEntity entity = this.lambdaQuery()
                .eq(AnswerEntity::getId, questionId)
                .one();
        if (entity == null) {
            throw new NotFoundException(AnswerEntity.class, questionId.toString());
        }
        return converter.toDto(entity);
    }

    private void checkCreator(Long questionId, Long userId) {
        AnswerEntity entity = this.lambdaQuery()
                .eq(AnswerEntity::getId, questionId)
                .select(AnswerEntity::getCreator).one();
        if (entity == null) {
            throw new NotFoundException(AnswerEntity.class, questionId.toString());
        }
        if (!entity.getCreator().equals(userId)) {
            throw new NotCreatorException(userId, questionId);
        }
    }

    public PageDTO<AnswerDTO> page(Long cursorId, int size) {
        List<AnswerDTO> records = this.lambdaQuery()
                .gt(AnswerEntity::getId, cursorId)
                .orderByDesc(AnswerEntity::getId)
                .last(String.format("LIMIT %d", size))
                .list()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
        return PageDTO.<AnswerDTO>builder()
                .records(records)
                .pageSize(size)
                .build();
    }
}
