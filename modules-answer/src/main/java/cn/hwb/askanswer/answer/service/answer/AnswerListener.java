package cn.hwb.askanswer.answer.service.answer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author hwb
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AnswerListener {
    private final AnswerService answerService;
}
