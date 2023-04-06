package cn.hwb.askanswer.question.service.question;

import cn.hwb.askanswer.common.base.pojo.event.NewAnswerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class QuestionEventListener {
    private final QuestionService questionService;

    @EventListener(NewAnswerEvent.class)
    public void handleQuestionEvent(NewAnswerEvent event) {
        log.info("QuestionEvent:{}", event);
        questionService.afterBeAnswered(event.getQuestionId(), event.getUserId());
    }
}
