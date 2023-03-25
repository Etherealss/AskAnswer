package cn.hwb.askanswer.comment.controller;

import cn.hwb.askanswer.comment.service.comment.CommentService;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.validation.entity.EntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import cn.hwb.common.security.xss.XssEscape;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateCommentRequest;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.UpdateCommentRequest;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Slf4j
@RestController
@RequestMapping("/questions/{questionId}/answers/{answerId}")
@RequiredArgsConstructor
@ResponseAdvice
public class CommentController {

    private static final String QUESTION = "questionEntityValidator";
    private static final String ANSWER = "answerEntityValidator";

    private final CommentService commentService;

    @PostMapping("/comments")
    @XssEscape
    @EntityExist
    public Long publish(@RequestBody @Validated CreateCommentRequest req,
                        @PathVariable @EntityExist(QUESTION) Long questionId,
                        @PathVariable @EntityExist(ANSWER) Long answerId) {
        return commentService.publish(questionId, req);
    }

    @PutMapping("/comments/{commentId}")
    @XssEscape
    @EntityExist
    public void update(@RequestBody @Validated UpdateCommentRequest req,
                       @PathVariable @EntityExist(QUESTION) Long questionId,
                       @PathVariable @EntityExist(ANSWER) Long answerId,
                       @PathVariable Long commentId) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        commentService.update(answerId, userId, req);
    }

    @DeleteMapping("/comments/{commentId}")
    public void delete(@PathVariable Long commentId) {
        commentService.deleteById(commentId,
                UserSecurityContextHolder.require().getUserId());
    }

    @GetMapping("/comments/{commentId}")
    @AnonymousAccess
    @EntityExist
    public CommentDTO findById(@PathVariable @EntityExist(QUESTION) Long questionId,
                               @PathVariable @EntityExist(ANSWER) Long answerId,
                               @PathVariable Long commentId) {
        return commentService.findById(answerId);
    }

    @GetMapping("/pages/comments")
    @AnonymousAccess
    @EntityExist
    public PageDTO<CommentDTO> page(
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable @EntityExist(QUESTION) Long questionId,
            @PathVariable @EntityExist(ANSWER) Long answerId) {
        if (size < 0) {
            log.debug("分页的size不能小于0，size: {}", size);
            size = 10;
        }
        return commentService.page(cursor, size);
    }
}
