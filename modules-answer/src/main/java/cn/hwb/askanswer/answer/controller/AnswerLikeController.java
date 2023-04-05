package cn.hwb.askanswer.answer.controller;

import cn.hwb.askanswer.common.base.validation.entity.EntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.like.service.LikeService;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wtk
 * @date 2023-04-05
 */
@Slf4j
@RestController
@RequestMapping("/answers/{answerId}/likes/relations")
@RequiredArgsConstructor
@ResponseAdvice
public class AnswerLikeController {
    private static final String DEFAULT = "answerEntityValidator";

    private final LikeService likeService;

    @PostMapping
    public void addLike(@PathVariable @EntityExist(DEFAULT) Long answerId) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        likeService.like(userId, answerId);
    }
}
