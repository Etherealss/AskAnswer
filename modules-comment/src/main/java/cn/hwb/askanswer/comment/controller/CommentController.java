package cn.hwb.askanswer.comment.controller;

import cn.hwb.askanswer.comment.infrastructure.pojo.dto.CommentDTO;
import cn.hwb.askanswer.comment.service.comment.CommentService;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.validation.entity.EntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 评论接口的RESTful完整接口是 /questions/{questionId}/answers/{answerId}/comments
 * 但：
 * 1. 这种写法需要校验 questionId 和 answerId 对应的实体是否存在，还需要检查 answerId 对应的回答目标是否为 questionId 的回答，存在较大的不便之处
 * 2. 发布 answer 的时候已经对 answer与question 的关系进行了保证，此处属于重复验证
 * 3. 判断 question 需要检查数据库，存在性能问题
 * 4. 深层的 RESTful 接口在前后端交互的时候存在不便
 * 综上，对RESTful接口进行了简化，仅保留了与评论相关的answer部分，去掉了question部分
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping("/answers/{answerId}")
@RequiredArgsConstructor
@ResponseAdvice
public class CommentController {

    private static final String ANSWER = "answerEntityValidator";

    private final CommentService commentService;

    @DeleteMapping("/comments/{commentId}")
    public void delete(@PathVariable Long commentId) {
        commentService.deleteById(commentId,
                UserSecurityContextHolder.require().getUserId());
    }

    @GetMapping("/comments/{commentId}")
    @AnonymousAccess
    @EntityExist
    public CommentDTO findById(@PathVariable @EntityExist(ANSWER) Long answerId,
                               @PathVariable Long commentId) {
        return commentService.findById(answerId, commentId);
    }

    @GetMapping("/pages/comments")
    @AnonymousAccess
    @EntityExist
    public PageDTO<CommentDTO> page(
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "current", defaultValue = "1") int currentPage,
            @PathVariable @EntityExist(ANSWER) Long answerId) {
        if (size < 1) {
            log.debug("分页的size不能小于1，size: {}", size);
            size = 10;
        }
        return commentService.page(currentPage, size, answerId, false);
    }
}
