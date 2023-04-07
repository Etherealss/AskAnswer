package cn.hwb.askanswer.collection.controller;

import cn.hwb.askanswer.collection.service.CollectionCountService;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.auth.annotation.AnonymousAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hwb
 */
@Slf4j
@RestController
@RequestMapping("/collections/counts")
@RequiredArgsConstructor
@ResponseAdvice
public class CollectionCountController {
    private final CollectionCountService collectionCountService;

    @AnonymousAccess
    @GetMapping("/{targetId}")
    public Integer get(@PathVariable Long targetId) {
        return collectionCountService.get(targetId);
    }
}
