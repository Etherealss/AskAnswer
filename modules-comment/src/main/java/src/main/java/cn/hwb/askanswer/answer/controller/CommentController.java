package src.main.java.cn.hwb.askanswer.answer.controller;

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
import src.main.java.cn.hwb.askanswer.answer.infrastructure.pojo.dto.CommentDTO;
import src.main.java.cn.hwb.askanswer.answer.infrastructure.pojo.request.CreateCommentRequest;
import src.main.java.cn.hwb.askanswer.answer.infrastructure.pojo.request.UpdateCommentRequest;
import src.main.java.cn.hwb.askanswer.answer.service.comment.CommentService;

/**
 * @author wtk
 * @date 2023-03-23
 */
@Slf4j
@RestController
@RequestMapping("/questions/{questionId}/answers/{answerId}")
@RequiredArgsConstructor
@ResponseAdvice
@Validated
public class CommentController {

    private static final String QUESTION = "questionEntityValidator";
    private static final String ANSWER = "answerEntityValidator";

    private final CommentService commentService;

    @PostMapping("/comments")
    @XssEscape
    public Long publish(@RequestBody @Validated CreateCommentRequest req,
                        @PathVariable @CheckEntityExist(QUESTION) Long questionId,
                        @PathVariable @CheckEntityExist(ANSWER) Long answerId) {
        return commentService.publish(questionId, req);
    }

    @PutMapping("/comments/{commentId}")
    @XssEscape
    public void update(@RequestBody @Validated UpdateCommentRequest req,
                       @PathVariable @CheckEntityExist(QUESTION) Long questionId,
                       @PathVariable @CheckEntityExist(ANSWER) Long answerId,
                       @PathVariable Long commentId) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        commentService.update(answerId, userId, req);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteQuestion(@PathVariable Long commentId) {
        commentService.deleteById(commentId,
                UserSecurityContextHolder.require().getUserId());
    }

    @GetMapping("/comments/{commentId}")
    @AnonymousAccess
    public CommentDTO findById(@PathVariable @CheckEntityExist(QUESTION) Long questionId,
                               @PathVariable @CheckEntityExist(ANSWER) Long answerId,
                               @PathVariable Long commentId) {
        return commentService.findById(answerId);
    }

    @GetMapping("/pages/comments")
    @AnonymousAccess
    public PageDTO<CommentDTO> page(
            @RequestParam(value = "cursor", defaultValue = "0") Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable @CheckEntityExist(QUESTION) Long questionId,
            @PathVariable @CheckEntityExist(ANSWER) Long answerId) {
        if (size < 0) {
            log.debug("分页的size不能小于0，size: {}", size);
            size = 10;
        }
        return commentService.page(cursor, size);
    }
}
