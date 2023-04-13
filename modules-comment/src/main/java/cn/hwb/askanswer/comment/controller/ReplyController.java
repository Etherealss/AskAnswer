package cn.hwb.askanswer.comment.controller;

import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.infrastructure.pojo.request.CreateReplyRequest;
import cn.hwb.askanswer.comment.service.comment.CommentService;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.validation.entity.EntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping("/comments/{commentId}")
@RequiredArgsConstructor
@ResponseAdvice
public class ReplyController {

    private static final String COMMENT = "commentEntityValidator";

    private final CommentService commentService;

    @PostMapping("/replys")
    @EntityExist
    public Long publish(@RequestBody @Validated CreateReplyRequest req,
                        @PathVariable Long commentId) {
        return commentService.publishReply(commentId, req);
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
        if (size < 1) {
            log.debug("分页的size不能小于1，size: {}", size);
            size = 10;
        }
        return commentService.page(currentPage, size, commentId, subReply);
    }
}
