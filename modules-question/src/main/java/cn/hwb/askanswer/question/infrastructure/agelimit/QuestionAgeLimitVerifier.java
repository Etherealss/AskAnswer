package cn.hwb.askanswer.question.infrastructure.agelimit;

import cn.hwb.askanswer.common.base.enums.AgeBracketEnum;
import cn.hwb.askanswer.question.service.question.QuestionService;
import cn.hwb.common.security.agelimit.AgeLimitVerifier;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wtk
 * @date 2023-04-05
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class QuestionAgeLimitVerifier implements AgeLimitVerifier {
    private final QuestionService questionService;

    @Override
    public boolean verify(Long id) {
        AgeBracketEnum ageBracketEnum = questionService.findAgeById(id);
        Date birthday = UserSecurityContextHolder.require().getBirthday();
        AgeBracketEnum userAge = AgeBracketEnum.getByBirthday(birthday);
        return userAge.getMinAge() >= ageBracketEnum.getMinAge();
    }
}
