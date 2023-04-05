package cn.hwb.askanswer.like.controller;

import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.askanswer.like.service.LikeCountService;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wtk
 * @date 2023-04-05
 */
@Slf4j
@RestController
@RequestMapping("/likes/counts")
@RequiredArgsConstructor
@ResponseAdvice
public class LikeCountController {
    private final LikeCountService likeCountService;

    @AnonymousAccess
    @GetMapping("/{targetId}")
    public Integer get(@PathVariable Long targetId) {
        return likeCountService.get(targetId);
    }
}
