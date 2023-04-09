package cn.hwb.askanswer.answer.controller;

import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.service.answer.AnswerService;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.validation.entity.EntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@ResponseAdvice
public class AnswerLikeController {
    private static final String ANSWER = "answerEntityValidator";

    private final AnswerService answerService;

    /**
     * TODO 还是改成 /questions/{questionId}/answers/{answerId}/likes/relations ?
     * @param answerId
     */
    @PostMapping("/answers/{answerId}/likes/relations")
    public void addLike(@PathVariable Long answerId) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        answerService.answerBeLiked(answerId, userId);
    }

    @GetMapping("/users/{userId}/pages/answers/likes/relations")
    @AnonymousAccess
    @EntityExist
    public PageDTO<AnswerDTO> pageByLikes(
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable Long userId) {
        if (size < 1) {
            log.debug("分页的size不能小于1，size: {}", size);
            size = 10;
        }
        return answerService.pageByLikes(userId, cursor, size);
    }
}
