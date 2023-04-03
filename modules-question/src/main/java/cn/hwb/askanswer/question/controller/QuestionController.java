package cn.hwb.askanswer.question.controller;

import cn.hwb.askanswer.common.base.enums.AgeBracketEnum;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.question.infrastructure.pojo.dto.QuestionDTO;
import cn.hwb.askanswer.question.infrastructure.pojo.request.CreateQuestionRequest;
import cn.hwb.askanswer.question.infrastructure.pojo.request.UpdateQuestionRequest;
import cn.hwb.askanswer.question.service.question.QuestionService;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import cn.hwb.common.security.xss.XssEscape;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@ResponseAdvice
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/questions")
    @XssEscape
    public Long publish(@RequestBody @Validated CreateQuestionRequest req) {
        Date birthday = UserSecurityContextHolder.require().getBirthday();
        return questionService.publish(req, AgeBracketEnum.getByBirthday(birthday));
    }

    @PutMapping("/questions/{questionId}")
    @XssEscape
    public void update(@RequestBody @Validated UpdateQuestionRequest req,
                       @PathVariable Long questionId) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        questionService.update(questionId, userId, req);
    }

    @DeleteMapping("/questions/{questionId}")
    public void deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteById(questionId,
                UserSecurityContextHolder.require().getUserId());
    }

    @GetMapping("/questions/{questionId}")
    @AnonymousAccess
    public QuestionDTO findById(@PathVariable Long questionId) {
        return questionService.findById(questionId);
    }

    @GetMapping("/pages/questions")
    @AnonymousAccess
    public PageDTO<QuestionDTO> page(
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        if (size < 0) {
            log.debug("分页的size不能小于0，size: {}", size);
            size = 10;
        }
        return questionService.page(cursor, size);
    }
}
