package cn.hwb.askanswer.collection.controller;

import cn.hwb.askanswer.collection.service.CollectionRelationService;
import cn.hwb.askanswer.collection.service.CollectionService;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author wtk
 * @date 2023-04-05
 */
@Slf4j
@RestController
@RequestMapping("/collections/relations")
@RequiredArgsConstructor
@ResponseAdvice
public class CollectionRelationController {
    private final CollectionRelationService collectionRelationService;
    private final CollectionService collectionService;

    @GetMapping("/{targetId}")
    public Boolean checkLike(@PathVariable Long targetId) {
        return collectionRelationService.isLiked(
                UserSecurityContextHolder.require().getUserId(),
                targetId
        );
    }

    @DeleteMapping("/{targetId}")
    public void dislike(@PathVariable Long targetId) {
        collectionService.dislike(
                UserSecurityContextHolder.require().getUserId(),
                targetId
        );
    }
}
