package cn.hwb.askanswer.question.infrastructure.event.listener;

import cn.hwb.askanswer.common.base.pojo.event.QuestionCreatorValidateEvent;
import cn.hwb.askanswer.question.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author wtk
 * @date 2023-03-25
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QuestionEventListener {
    private final QuestionService questionService;

    @EventListener
    public void handleEvent(QuestionCreatorValidateEvent event) {
        questionService.checkCreator(event.getQuestionId(), event.getUserId());
    }
}
