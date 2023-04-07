package cn.hwb.askanswer.question.infrastructure.event.listener;

import cn.hwb.askanswer.common.base.pojo.event.AnswerAgeLimitEvent;
import cn.hwb.askanswer.common.base.pojo.event.AnswerPublishedEvent;
import cn.hwb.askanswer.common.base.pojo.event.QuestionCreatorValidateEvent;
import cn.hwb.askanswer.question.service.question.QuestionService;
import cn.hwb.common.security.auth.exception.AgeLimitedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author hwb
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QuestionEventListener {
    private final QuestionService questionService;

    @EventListener(QuestionCreatorValidateEvent.class)
    public void handle(QuestionCreatorValidateEvent event) {
        log.trace("QuestionCreatorValidateEvent: {}", event);
        questionService.checkCreator(event.getQuestionId(), event.getUserId());
    }

    @EventListener(AnswerPublishedEvent.class)
    public void handle(AnswerPublishedEvent event) {
        log.trace("AnswerPublishedEvent: {}", event);
        questionService.afterBeAnswered(event.getQuestionId());
    }

    @EventListener(AnswerAgeLimitEvent.class)
    public void handle(AnswerAgeLimitEvent event) {
        log.trace("AnswerAgeLimitEvent: {}", event);
        int requiredMinAge = questionService.findAgeById(event.getQuestionId()).getMinAge();
        int userMinAge = event.getAgeBracket().getMinAge();
        if (requiredMinAge > userMinAge) {
            throw new AgeLimitedException("当前用户年龄段受限，不允许回答问题");
        }
    }
}
