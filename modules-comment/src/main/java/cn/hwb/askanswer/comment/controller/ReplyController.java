package cn.hwb.askanswer.comment.controller;

import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateReplyRequest;
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

/**
 * @author wtk
 * @date 2023-03-23
 */
@Slf4j
@RestController
@RequestMapping("/questions/{questionId}/answers/{answerId}/comments/{commentId}")
@RequiredArgsConstructor
@ResponseAdvice
public class ReplyController {

    private static final String COMMENT = "commentEntityValidator";

    private final CommentService commentService;

    @PostMapping("/replys")
    @XssEscape
    @EntityExist
    public Long publish(@RequestBody @Validated CreateReplyRequest req,
                        @PathVariable @EntityExist(COMMENT) Long commentId) {
        return commentService.publish(commentId, req);
    }

    @DeleteMapping("/replys/{replyId}")
    public void delete(@PathVariable Long replyId) {
        commentService.deleteById(replyId,
                UserSecurityContextHolder.require().getUserId());
    }

    @GetMapping("/replys/{replyId}")
    @AnonymousAccess
    @EntityExist
    public CommentDTO findById(@PathVariable @EntityExist(COMMENT) Long commentId,
                               @PathVariable Long replyId) {
        return commentService.findById(commentId, replyId);
    }

    @GetMapping("/pages/replys")
    @AnonymousAccess
    @EntityExist
    public PageDTO<CommentDTO> page(
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "current", defaultValue = "1") int currentPage,
            @RequestParam(value = "subReply", defaultValue = "false") boolean subReply,
            @PathVariable @EntityExist(COMMENT) Long commentId) {
        if (size < 0) {
            log.debug("分页的size不能小于0，size: {}", size);
            size = 10;
        }
        return commentService.page(currentPage, size, commentId, subReply);
    }
}
