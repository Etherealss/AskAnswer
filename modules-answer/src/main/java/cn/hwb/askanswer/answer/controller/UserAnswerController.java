package cn.hwb.askanswer.answer.controller;

import cn.hwb.askanswer.answer.infrastructure.pojo.dto.AnswerDTO;
import cn.hwb.askanswer.answer.service.answer.AnswerService;
import cn.hwb.askanswer.common.base.pojo.dto.PageDTO;
import cn.hwb.askanswer.common.base.validation.entity.EntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
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
public class UserAnswerController {

    private final AnswerService answerService;

    /**
     * 获取某一用户的回答列表
     */
    @GetMapping("/users/{userId}/pages/answers")
    @AnonymousAccess
    @EntityExist
    public PageDTO<AnswerDTO> pageByUser(
            @RequestParam(value = "cursor", defaultValue = "" + Long.MAX_VALUE) Long cursor,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable Long userId) {
        if (size < 1) {
            log.debug("分页的size不能小于1，size: {}", size);
            size = 10;
        }
        return answerService.pageByUser(cursor, size, userId);
    }

    @GetMapping("/users/{userId}/count/answers")
    @AnonymousAccess
    public Integer countByUser(@PathVariable Long userId) {
        return answerService.countByUser(userId);
    }
}
