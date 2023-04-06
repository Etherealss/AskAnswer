package cn.hwb.askanswer.answer.service.answer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AnswerListener {
    private final AnswerService answerService;
}
