package cn.hwb.askanswer.answer.controller;

import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.service.answer.AnswerService;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.validation.entity.EntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.like.infrastructure.enums.LikeTargetType;
import cn.hwb.askanswer.like.service.LikeService;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author wtk
 * @date 2023-04-05
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@ResponseAdvice
public class AnswerLikeController {
    private static final String ANSWER = "answerEntityValidator";

    private final LikeService likeService;
    private final AnswerService answerService;

    /**
     * TODO 还是改成 /questions/{questionId}/answers/{answerId}/likes/relations ?
     * @param answerId
     */
    @PostMapping("/answers/{answerId}/likes/relations")
    public void addLike(@PathVariable @EntityExist(ANSWER) Long answerId) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        likeService.like(userId, answerId, LikeTargetType.ANSWER);
    }

    @GetMapping("/pages/answers/likes/relations")
    @EntityExist
    public PageDTO<AnswerDTO> pageByLikes(
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        if (size < 0) {
            log.debug("分页的size不能小于0，size: {}", size);
            size = 10;
        }
        Long userId = UserSecurityContextHolder.require().getUserId();
        return answerService.pageByLikes(userId, cursor, size);
    }
}
