package cn.hwb.askanswer.answer.controller;

import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.CreateAnswerRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerAcceptRequest;
import cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateAnswerRequest;
import cn.hwb.askanswer.answer.service.answer.AnswerService;
import cn.hwb.askanswer.common.base.enums.AgeBracketEnum;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.validation.entity.EntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping("/questions/{questionId}")
@RequiredArgsConstructor
@ResponseAdvice
public class AnswerController {

    private static final String QUESTION = "questionEntityValidator";
    private final AnswerService answerService;

    @PostMapping("/answers")
    @EntityExist
    public Long publish(@RequestBody @Validated CreateAnswerRequest req,
                        @PathVariable @EntityExist(QUESTION) Long questionId) {
        Long answerCreator = UserSecurityContextHolder.require().getUserId();
        AgeBracketEnum ageBracket = UserSecurityContextHolder.require().getAgeBracket();
        return answerService.publish(questionId, answerCreator, ageBracket, req);
    }

    @PutMapping("/answers/{answersId}")
    @EntityExist
    public void update(@RequestBody @Validated UpdateAnswerRequest req,
                       @PathVariable @EntityExist(QUESTION) Long questionId,
                       @PathVariable Long answersId) {
        Long answerCreator = UserSecurityContextHolder.require().getUserId();
        answerService.update(questionId, answersId, answerCreator, req);
    }

    @PutMapping("/answers/{answersId}/accepted")
    @EntityExist
    public void accpetAnswer(@RequestBody @Validated UpdateAnswerAcceptRequest req,
                             @PathVariable @EntityExist(QUESTION) Long questionId,
                             @PathVariable Long answersId) {
        Long questionCreator = UserSecurityContextHolder.require().getUserId();
        answerService.update(questionId, answersId, questionCreator, req);
    }

    @DeleteMapping("/answers/{answersId}")
    public void deleteAnswer(@PathVariable Long answersId) {
        Long answerCreator = UserSecurityContextHolder.require().getUserId();
        answerService.deleteById(answersId, answerCreator);
    }

    @GetMapping("/answers/{answersId}")
    @AnonymousAccess
    @EntityExist
    public AnswerDTO findById(@PathVariable Long answersId,
                              @PathVariable @EntityExist(QUESTION) Long questionId) {
        return answerService.findById(answersId);
    }

    @GetMapping("/pages/answers")
    @AnonymousAccess
    @EntityExist
    public PageDTO<AnswerDTO> page(
            @RequestParam(value = "cursor", defaultValue = "" + Long.MAX_VALUE) Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable @EntityExist(QUESTION) Long questionId) {
        if (size < 1) {
            log.debug("分页的size不能小于1，size: {}", size);
            size = 10;
        }
        return answerService.pageByQuestion(cursor, size, questionId);
    }
}
