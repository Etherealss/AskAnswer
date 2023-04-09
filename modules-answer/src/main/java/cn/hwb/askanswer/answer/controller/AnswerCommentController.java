package cn.hwb.askanswer.answer.controller;

import cn.hwb.askanswer.answer.service.answer.AnswerService;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateCommentRequest;
import cn.hwb.askanswer.common.base.validation.entity.EntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.xss.XssEscape;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping("/answers/{answerId}")
@RequiredArgsConstructor
@ResponseAdvice
public class AnswerCommentController {

    private final AnswerService answerService;

    /**
     * 对回答进行评论
     * @param req
     * @param answerId
     * @return
     */
    @PostMapping("/comments")
    @XssEscape
    @EntityExist
    public Long publish(@RequestBody @Validated CreateCommentRequest req,
                        @PathVariable Long answerId) {
        return answerService.publishComment(answerId, req);
    }

}
