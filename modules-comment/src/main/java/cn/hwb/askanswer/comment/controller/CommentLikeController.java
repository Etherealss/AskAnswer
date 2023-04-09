package cn.hwb.askanswer.comment.controller;

import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.service.comment.CommentService;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
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
    private final CommentService commentService;

    @PostMapping("/comment/{commentId}/likes/relations")
    public void addLike(@PathVariable Long commentId) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        commentService.like(userId, commentId);
    }

    @GetMapping("/users/{userId}/pages/comments/likes/relations")
    public PageDTO<CommentDTO> page(
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable Long userId) {
        if (size < 1) {
            log.debug("分页的size不能小于1，size: {}", size);
            size = 10;
        }
        return commentService.pageByLike(userId, cursor, size);
    }

}
