package cn.hwb.askanswer.comment.controller;

import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.service.comment.CommentService;
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
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@ResponseAdvice
public class CommentLikeController {
    private static final String DEFAULT = "commentEntityValidator";

    private final LikeService likeService;
    private final CommentService commentService;

    @PostMapping("/comment/{commentId}/likes/relations")
    public void addLike(@PathVariable @EntityExist(DEFAULT) Long commentId) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        likeService.like(userId, commentId, LikeTargetType.COMMENT);
    }

    @GetMapping("/pages/comments/likes/relations")
    public PageDTO<CommentDTO> page(
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        if (size < 0) {
            log.debug("分页的size不能小于0，size: {}", size);
            size = 10;
        }
        Long userId = UserSecurityContextHolder.require().getUserId();
        return commentService.pageByLike(userId, cursor, size);
    }

}
