package cn.hwb.askanswer.answer.controller;

import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.CreateAnswerRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerRequest;
import cn.hwb.askanswer.answer.service.answer.AnswerService;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.validation.CheckEntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import cn.hwb.common.security.xss.XssEscape;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Slf4j
@RestController
@RequestMapping("/questions/{questionId}")
@RequiredArgsConstructor
@ResponseAdvice
@Validated
public class AnswerController {

    private static final String DEFAULT = "questionEntityValidator";
    private final AnswerService answerService;

    @PostMapping("/answers")
    @XssEscape
    public Long publish(@RequestBody @Validated CreateAnswerRequest req,
                        @PathVariable @CheckEntityExist(DEFAULT) Long questionId) {
        return answerService.publish(questionId, req);
    }

    @PutMapping("/answers/{answersId}")
    @XssEscape
    public void update(@RequestBody @Validated UpdateAnswerRequest req,
                       @PathVariable Long answersId) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        answerService.update(answersId, userId, req);
    }

    @DeleteMapping("/answers/{answersId}")
    public void deleteQuestion(@PathVariable Long answersId) {
        answerService.deleteById(answersId,
                UserSecurityContextHolder.require().getUserId());
    }

    @GetMapping("/answers/{answersId}")
    @AnonymousAccess
    public AnswerDTO findById(@PathVariable Long answersId,
                              @PathVariable @CheckEntityExist(DEFAULT) Long questionId) {
        return answerService.findById(answersId);
    }

    @GetMapping("/pages/answers")
    @AnonymousAccess
    public PageDTO<AnswerDTO> page(
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable @CheckEntityExist(DEFAULT) Long questionId) {
        if (size < 0) {
            log.debug("分页的size不能小于0，size: {}", size);
            size = 10;
        }
        return answerService.page(cursor, size);
    }
}
