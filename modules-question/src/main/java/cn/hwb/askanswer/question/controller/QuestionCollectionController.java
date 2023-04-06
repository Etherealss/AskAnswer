package cn.hwb.askanswer.question.controller;

import cn.hwb.askanswer.collection.service.CollectionService;
import cn.hwb.askanswer.common.base.validation.entity.EntityExist;
import cn.hwb.askanswer.common.base.web.ResponseAdvice;
import cn.hwb.common.security.token.user.UserSecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wang tengkun
 * @date 2023/4/6
 */
@Slf4j
@RestController
@RequestMapping("/questions/{questionId}/collections/relations")
@RequiredArgsConstructor
@ResponseAdvice
public class QuestionCollectionController {
    private static final String DEFAULT = "answerEntityValidator";

    private final CollectionService collectionService;

    @PostMapping
    public void addRelation(@PathVariable @EntityExist(DEFAULT) Long questionId) {
        Long userId = UserSecurityContextHolder.require().getUserId();
        collectionService.addCollection(userId, questionId);
    }
}