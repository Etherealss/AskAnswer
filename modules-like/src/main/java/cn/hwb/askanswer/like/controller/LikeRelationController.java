package cn.hwb.askanswer.like.controller;

import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.like.service.LikeRelationService;
import cn.hwb.askanswer.like.service.LikeService;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping("/likes/relations")
@RequiredArgsConstructor
@ResponseAdvice
public class LikeRelationController {
    private final LikeRelationService likeRelationService;
    private final LikeService likeService;

    @GetMapping("/{targetId}")
    public Boolean checkLike(@PathVariable Long targetId) {
        return likeRelationService.isLiked(
                UserSecurityContextHolder.require().getUserId(),
                targetId
        );
    }

    @DeleteMapping("/{targetId}")
    public void dislike(@PathVariable Long targetId) {
        likeService.dislike(
                UserSecurityContextHolder.require().getUserId(),
                targetId
        );
    }
}
